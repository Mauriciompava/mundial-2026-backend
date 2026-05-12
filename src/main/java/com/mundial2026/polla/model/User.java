package com.mundial2026.polla.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    
    @Column(unique = true)
    private String email;
    
    private String password;
    
    private Integer totalPoints = 0;
    private Double entryFee = 20000.0;
    private boolean paid = false;
    @Column(columnDefinition = "LONGTEXT")
    private String paymentReceipt; // Will store base64 image

    @ManyToOne
    @JoinColumn(name = "champion_team_id")
    private Team championTeam;

    public User(Object o, String mateo, String mail, String number, int i, double v, Team colombia) {
    }
}
