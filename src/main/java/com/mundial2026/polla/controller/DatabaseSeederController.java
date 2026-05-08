package com.mundial2026.polla.controller;

import com.mundial2026.polla.model.Match;
import com.mundial2026.polla.model.Team;
import com.mundial2026.polla.repository.MatchRepository;
import com.mundial2026.polla.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class DatabaseSeederController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @PostMapping("/seed")
    public String seedDatabase() {
        // ... existing code ...
        return "Base de datos inicializada con equipos de prueba.";
    }

    @PostMapping("/seed-raw")
    public String seedRawSql(@org.springframework.web.bind.annotation.RequestBody String sql) {
        try {
            // Dividir el script por punto y coma, ignorando comentarios y líneas vacías
            String[] statements = sql.split(";");
            for (String statement : statements) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--") && !trimmed.startsWith("/*")) {
                    jdbcTemplate.execute(trimmed);
                }
            }
            return "¡Éxito! El script SQL se ejecutó correctamente.";
        } catch (Exception e) {
            return "Error al ejecutar el script: " + e.getMessage();
        }
    }
}
