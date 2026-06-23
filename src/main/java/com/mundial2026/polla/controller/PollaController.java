package com.mundial2026.polla.controller;

import com.mundial2026.polla.model.Match;
import com.mundial2026.polla.model.Prediction;
import com.mundial2026.polla.service.PollaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PollaController {

    @Autowired
    private PollaService pollaService;

    @Autowired
    private com.mundial2026.polla.repository.UserRepository userRepo;
    
    @Autowired
    private com.mundial2026.polla.repository.PredictionRepository predictionRepo;

    @GetMapping("/matches")
    public List<Match> getMatches() {
        return pollaService.getAllMatches();
    }

    @GetMapping("/stats/users")
    public long getUserCount() {
        return userRepo.countByPaid(true);
    }

    @GetMapping("/stats/predictions")
    public long getPredictionCount() {
        return predictionRepo.count();
    }

    @GetMapping("/stats/income")
    public double getTotalIncome() {
        Double income = userRepo.getTotalIncomeForPaidUsers();
        return income != null ? income : 0.0;
    }

    @GetMapping("/stats/total-points")
    public long getTotalPoints() {
        Long points = userRepo.getTotalPointsSum();
        return points != null ? points : 0;
    }

    @GetMapping("/stats/matches-count")
    public long getMatchesCount() {
        return pollaService.getAllMatches().size();
    }

    @GetMapping("/stats/finished-matches")
    public long getFinishedMatchesCount() {
        return pollaService.getAllMatches().stream()
                .filter(m -> m.getStatus() == Match.MatchStatus.FINISHED)
                .count();
    }

    @PostMapping("/predictions")
    public Prediction createPrediction(@RequestBody Prediction prediction) {
        return pollaService.savePrediction(prediction);
    }

    @GetMapping("/predictions/check")
    public Prediction checkPrediction(@RequestParam Long userId, @RequestParam Long matchId) {
        return predictionRepo.findByUserIdAndMatchId(userId, matchId).orElse(null);
    }

    @GetMapping("/predictions/user/{userId}")
    public List<Prediction> getUserPredictions(@PathVariable Long userId) {
        return predictionRepo.findByUserId(userId);
    }

    @PostMapping("/matches/{id}/result")
    public void updateResult(@PathVariable Long id, @RequestParam Integer homeScore, @RequestParam Integer awayScore) {
        pollaService.updateMatchResult(id, homeScore, awayScore);
    }

    @PostMapping("/matches/{id}/reset")
    public Match resetMatch(@PathVariable Long id) {
        return pollaService.resetMatch(id);
    }
}
