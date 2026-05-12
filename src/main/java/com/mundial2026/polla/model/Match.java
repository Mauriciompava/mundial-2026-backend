package com.mundial2026.polla.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    private LocalDateTime matchDate;
    
    private Integer homeScore;
    private Integer awayScore;
    
    private String stage; // e.g., "Group Stage", "Round of 16", etc.
    
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @JsonProperty("date")
    public String getFormattedDate() {
        return matchDate != null ? matchDate.format(DateTimeFormatter.ofPattern("dd MMM")) : "";
    }

    @JsonProperty("time")
    public String getFormattedTime() {
        return matchDate != null ? matchDate.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }

    public enum MatchStatus {
        SCHEDULED, LIVE, FINISHED
    }
}
