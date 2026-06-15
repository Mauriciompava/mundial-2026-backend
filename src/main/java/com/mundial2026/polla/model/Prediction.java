package com.mundial2026.polla.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "predictions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "match_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    private Integer predictedHomeScore;
    private Integer predictedAwayScore;
    
    private Integer pointsWon = 0;
}
