package com.mundial2026.polla.service;

import com.mundial2026.polla.model.Match;
import com.mundial2026.polla.model.Prediction;
import com.mundial2026.polla.model.Team;
import com.mundial2026.polla.model.User;
import com.mundial2026.polla.repository.MatchRepository;
import com.mundial2026.polla.repository.PredictionRepository;
import com.mundial2026.polla.repository.SystemSettingRepository;
import com.mundial2026.polla.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PollaService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemSettingRepository settingRepository;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Prediction savePrediction(Prediction prediction) {
        // 1. Validate user exists and has paid account
        User user = userRepository.findById(prediction.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!user.isPaid()) {
            throw new RuntimeException("Debes tener una cuenta activa para enviar pronósticos.");
        }

        // 2. Validate match exists and enforce 1-hour lock
        Match match = matchRepository.findById(prediction.getMatch().getId())
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
        if (match.getMatchDate() != null) {
            java.time.LocalDateTime cutoff = match.getMatchDate().minusMinutes(15);
            java.time.LocalDateTime nowColombia = java.time.LocalDateTime.now(java.time.ZoneId.of("America/Bogota"));
            if (nowColombia.isAfter(cutoff)) {
                throw new RuntimeException("Los pronósticos para este partido están cerrados (15 minutos antes del inicio).");
            }
        }
        if (match.getStatus() == Match.MatchStatus.FINISHED) {
            throw new RuntimeException("Este partido ya finalizó.");
        }

        // 3. Upsert: update existing prediction or create new one
        Prediction existing = predictionRepository.findByUserIdAndMatchId(user.getId(), match.getId()).orElse(null);

        if (existing != null) {
            existing.setPredictedHomeScore(prediction.getPredictedHomeScore());
            existing.setPredictedAwayScore(prediction.getPredictedAwayScore());
            return predictionRepository.save(existing);
        } else {
            prediction.setUser(user);
            prediction.setMatch(match);
            return predictionRepository.save(prediction);
        }
    }

    public void recalculateUserTotalPoints(User user) {
        int pointsFromPredictions = predictionRepository.findByUser(user).stream()
                .mapToInt(p -> p.getPointsWon() != null ? p.getPointsWon() : 0)
                .sum();

        int championPoints = 0;
        if (user.getChampionTeam() != null) {
            Match finalMatch = matchRepository.findAll().stream()
                    .filter(m -> "Final".equalsIgnoreCase(m.getStage()) && m.getStatus() == Match.MatchStatus.FINISHED)
                    .findFirst()
                    .orElse(null);
            if (finalMatch != null) {
                Team winner = null;
                if (finalMatch.getHomeScore() != null && finalMatch.getAwayScore() != null) {
                    if (finalMatch.getHomeScore() > finalMatch.getAwayScore()) {
                        winner = finalMatch.getHomeTeam();
                    } else if (finalMatch.getAwayScore() > finalMatch.getHomeScore()) {
                        winner = finalMatch.getAwayTeam();
                    }
                }
                if (winner != null && winner.getId().equals(user.getChampionTeam().getId())) {
                    championPoints = getSettingAsInt("pointsChampion", 100);
                }
            }
        }

        user.setTotalPoints(pointsFromPredictions + championPoints);
        userRepository.save(user);
    }

    @Transactional
    public void recalculateAllUsersTotalPoints() {
        cleanupDuplicatePredictions();
        List<User> users = userRepository.findAll();
        for (User u : users) {
            recalculateUserTotalPoints(u);
        }
    }

    @Transactional
    public void updateMatchResult(Long matchId, Integer homeScore, Integer awayScore) {
        cleanupDuplicatePredictions();
        Match match = matchRepository.findById(matchId).orElseThrow();
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus(Match.MatchStatus.FINISHED);
        matchRepository.save(match);

        // Fetch scoring rules
        int pExact = getSettingAsInt("pointsExact", 5);
        int pWinner = getSettingAsInt("pointsWinner", 3);
        int pDraw = getSettingAsInt("pointsDraw", 1);

        // Recalculate points for all predictions for this match
        List<Prediction> predictions = predictionRepository.findByMatchId(matchId);
        for (Prediction p : predictions) {
            int points = calculatePoints(p, homeScore, awayScore, pExact, pWinner, pDraw);
            p.setPointsWon(points);
            predictionRepository.save(p);
        }

        // Recalculate total points for all users
        recalculateAllUsersTotalPoints();
    }

    @Transactional
    public Match resetMatch(Long matchId) {
        cleanupDuplicatePredictions();
        Match match = matchRepository.findById(matchId).orElseThrow();
        
        // Reset match status and scores
        match.setHomeScore(null);
        match.setAwayScore(null);
        match.setStatus(Match.MatchStatus.SCHEDULED);
        matchRepository.save(match);

        // Find all predictions and reset points to 0
        List<Prediction> predictions = predictionRepository.findByMatchId(matchId);
        for (Prediction p : predictions) {
            p.setPointsWon(0);
            predictionRepository.save(p);
        }

        // Recalculate total points for all users
        recalculateAllUsersTotalPoints();

        return match;
    }

    private int getSettingAsInt(String key, int defaultValue) {
        return settingRepository.findById(key)
                .map(s -> Integer.parseInt(s.getSettingValue()))
                .orElse(defaultValue);
    }

    private int calculatePoints(Prediction p, int actualHome, int actualAway, int pExact, int pWinner, int pDraw) {
        if (p.getPredictedHomeScore() == null || p.getPredictedAwayScore() == null) {
            return 0;
        }
        int predHome = p.getPredictedHomeScore();
        int predAway = p.getPredictedAwayScore();

        // Exact score
        if (predHome == actualHome && predAway == actualAway) {
            return pExact;
        }

        // Tendency
        boolean actualDraw = actualHome == actualAway;
        boolean predDraw = predHome == predAway;
        
        if (actualDraw && predDraw) return pDraw;
        
        boolean actualHomeWin = actualHome > actualAway;
        boolean predHomeWin = predHome > predAway;
        boolean actualAwayWin = actualAway > actualHome;
        boolean predAwayWin = predAway > predHome;
        
        if ((actualHomeWin && predHomeWin) || (actualAwayWin && predAwayWin)) {
            return pWinner;
        }

        return 0;
    }
    
    @Transactional
    public void cleanupDuplicatePredictions() {
        List<Prediction> allPredictions = predictionRepository.findAll();
        java.util.Map<String, List<Prediction>> grouped = allPredictions.stream()
            .collect(java.util.stream.Collectors.groupingBy(p -> p.getUser().getId() + "_" + p.getMatch().getId()));
        
        for (List<Prediction> list : grouped.values()) {
            if (list.size() > 1) {
                list.sort(java.util.Comparator.comparing(Prediction::getId));
                for (int i = 0; i < list.size() - 1; i++) {
                    predictionRepository.delete(list.get(i));
                }
            }
        }
    }
    
    @Transactional
    public void deleteUser(Long userId) {
        predictionRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
