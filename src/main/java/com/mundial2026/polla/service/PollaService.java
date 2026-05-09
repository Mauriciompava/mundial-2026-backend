package com.mundial2026.polla.service;

import com.mundial2026.polla.model.Match;
import com.mundial2026.polla.model.Prediction;
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
        return predictionRepository.save(prediction);
    }

    @Transactional
    public void updateMatchResult(Long matchId, Integer homeScore, Integer awayScore) {
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

            // Update user total points
            User user = p.getUser();
            int currentPoints = user.getTotalPoints() != null ? user.getTotalPoints() : 0;
            user.setTotalPoints(currentPoints + points);
            userRepository.save(user);
        }
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
    public void deleteUser(Long userId) {
        predictionRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
