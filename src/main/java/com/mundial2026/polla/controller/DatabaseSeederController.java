package com.mundial2026.polla.controller;

import com.mundial2026.polla.service.PollaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class DatabaseSeederController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PollaService pollaService;

    @PostMapping("/seed")
    @org.springframework.transaction.annotation.Transactional
    public String seedDatabase() {
        // ⛔ PROTECCIÓN CRÍTICA: Este endpoint está deshabilitado permanentemente
        // para evitar pérdida de datos. La base de datos ya contiene datos de producción.
        return "⛔ BLOQUEADO: El seed de base de datos está deshabilitado para proteger datos existentes. " +
               "Los datos de producción (usuarios, predicciones, partidos) NO pueden ser reiniciados.";
    }

    @PostMapping("/set-real-schedules")
    public String setRealSchedules() {
        try {
            // Fecha 1 (Verificado y actualizado al 25 de junio de 2026)
            updateMatchTime("México", "Sudáfrica", "2026-06-11 14:00:00");
            updateMatchTime("Corea del Sur", "Chequia", "2026-06-11 21:00:00");
            updateMatchTime("Canadá", "Bosnia y Herzegovina", "2026-06-12 14:00:00");
            updateMatchTime("Estados Unidos", "Paraguay", "2026-06-12 20:00:00");
            updateMatchTime("Catar", "Suiza", "2026-06-13 14:00:00");
            updateMatchTime("Brasil", "Marruecos", "2026-06-13 17:00:00");
            updateMatchTime("Haití", "Escocia", "2026-06-13 20:00:00");
            updateMatchTime("Australia", "Turquía", "2026-06-13 23:00:00");
            updateMatchTime("Alemania", "Curazao", "2026-06-14 12:00:00");
            updateMatchTime("Países Bajos", "Japón", "2026-06-14 15:00:00");
            updateMatchTime("Costa de Marfil", "Ecuador", "2026-06-14 18:00:00");
            updateMatchTime("Suecia", "Túnez", "2026-06-14 21:00:00");
            updateMatchTime("España", "Cabo Verde", "2026-06-15 11:00:00");
            updateMatchTime("Bélgica", "Egipto", "2026-06-15 14:00:00");
            updateMatchTime("Arabia Saudí", "Uruguay", "2026-06-15 17:00:00");
            updateMatchTime("Irán", "Nueva Zelanda", "2026-06-15 20:00:00");
            updateMatchTime("Francia", "Senegal", "2026-06-16 14:00:00");
            updateMatchTime("Irak", "Noruega", "2026-06-16 17:00:00");
            updateMatchTime("Argentina", "Argelia", "2026-06-16 20:00:00");
            updateMatchTime("Austria", "Jordania", "2026-06-16 23:00:00");
            updateMatchTime("Portugal", "RD Congo", "2026-06-17 12:00:00");
            updateMatchTime("Inglaterra", "Croacia", "2026-06-17 15:00:00");
            updateMatchTime("Ghana", "Panamá", "2026-06-17 18:00:00");
            updateMatchTime("Uzbekistán", "Colombia", "2026-06-17 21:00:00");

            // Fecha 2
            updateMatchTime("Chequia", "Sudáfrica", "2026-06-18 11:00:00");
            updateMatchTime("Suiza", "Bosnia y Herzegovina", "2026-06-18 14:00:00");
            updateMatchTime("Canadá", "Catar", "2026-06-18 17:00:00");
            updateMatchTime("México", "Corea del Sur", "2026-06-18 20:00:00");
            updateMatchTime("Estados Unidos", "Australia", "2026-06-19 14:00:00");
            updateMatchTime("Escocia", "Marruecos", "2026-06-19 17:00:00");
            updateMatchTime("Brasil", "Haití", "2026-06-19 20:00:00");
            updateMatchTime("Turquía", "Paraguay", "2026-06-19 23:00:00");
            updateMatchTime("Países Bajos", "Suecia", "2026-06-20 14:00:00");
            updateMatchTime("Alemania", "Costa de Marfil", "2026-06-20 15:00:00");
            updateMatchTime("Ecuador", "Curazao", "2026-06-20 19:00:00");
            updateMatchTime("Túnez", "Japón", "2026-06-20 23:00:00");
            updateMatchTime("España", "Arabia Saudí", "2026-06-21 11:00:00");
            updateMatchTime("Bélgica", "Irán", "2026-06-21 14:00:00");
            updateMatchTime("Uruguay", "Cabo Verde", "2026-06-21 17:00:00");
            updateMatchTime("Nueva Zelanda", "Egipto", "2026-06-21 20:00:00");
            updateMatchTime("Argentina", "Austria", "2026-06-22 12:00:00");
            updateMatchTime("Francia", "Irak", "2026-06-22 16:00:00");
            updateMatchTime("Noruega", "Senegal", "2026-06-22 19:00:00");
            updateMatchTime("Jordania", "Argelia", "2026-06-22 22:00:00");
            updateMatchTime("Portugal", "Uzbekistán", "2026-06-23 12:00:00");
            updateMatchTime("Inglaterra", "Ghana", "2026-06-23 15:00:00");
            updateMatchTime("Panamá", "Croacia", "2026-06-23 18:00:00");
            updateMatchTime("Colombia", "RD Congo", "2026-06-23 21:00:00");

            // Fecha 3
            updateMatchTime("Suiza", "Canadá", "2026-06-24 14:00:00");
            updateMatchTime("Bosnia y Herzegovina", "Catar", "2026-06-24 14:00:00");
            updateMatchTime("Escocia", "Brasil", "2026-06-24 17:00:00");
            updateMatchTime("Marruecos", "Haití", "2026-06-24 17:00:00");
            updateMatchTime("Chequia", "México", "2026-06-24 20:00:00");
            updateMatchTime("Sudáfrica", "Corea del Sur", "2026-06-24 20:00:00");
            updateMatchTime("Curazao", "Costa de Marfil", "2026-06-25 15:00:00");
            updateMatchTime("Ecuador", "Alemania", "2026-06-25 15:00:00");
            updateMatchTime("Japón", "Suecia", "2026-06-25 18:00:00");
            updateMatchTime("Túnez", "Países Bajos", "2026-06-25 18:00:00");
            updateMatchTime("Turquía", "Estados Unidos", "2026-06-25 21:00:00");
            updateMatchTime("Paraguay", "Australia", "2026-06-25 21:00:00");
            updateMatchTime("Noruega", "Francia", "2026-06-26 14:00:00");
            updateMatchTime("Senegal", "Irak", "2026-06-26 14:00:00");
            updateMatchTime("Cabo Verde", "Arabia Saudí", "2026-06-26 19:00:00");
            updateMatchTime("Uruguay", "España", "2026-06-26 19:00:00");
            updateMatchTime("Egipto", "Irán", "2026-06-26 22:00:00");
            updateMatchTime("Nueva Zelanda", "Bélgica", "2026-06-26 22:00:00");
            updateMatchTime("Panamá", "Inglaterra", "2026-06-27 16:00:00");
            updateMatchTime("Croacia", "Ghana", "2026-06-27 16:00:00");
            updateMatchTime("Colombia", "Portugal", "2026-06-27 18:30:00");
            updateMatchTime("RD Congo", "Uzbekistán", "2026-06-27 18:30:00");
            updateMatchTime("Argelia", "Austria", "2026-06-27 21:00:00");
            updateMatchTime("Jordania", "Argentina", "2026-06-27 21:00:00");

            // Dieciseisavos (Eliminatoria de 32)
            updateKnockoutMatch(1072L, "Sudáfrica", "Canadá", "2026-06-28 14:00:00");
            updateKnockoutMatch(1073L, "Brasil", "Japón", "2026-06-29 12:00:00");
            updateKnockoutMatch(1074L, "Alemania", "Paraguay", "2026-06-29 15:30:00");
            updateKnockoutMatch(1075L, "Países Bajos", "Marruecos", "2026-06-29 20:00:00");
            updateKnockoutMatch(1076L, "Costa de Marfil", "Noruega", "2026-06-30 12:00:00");
            updateKnockoutMatch(1077L, "Francia", "Suecia", "2026-06-30 16:00:00");
            updateKnockoutMatch(1078L, "México", "Ecuador", "2026-06-30 20:00:00");
            updateKnockoutMatch(1079L, "Inglaterra", "RD Congo", "2026-07-01 11:00:00");
            updateKnockoutMatch(1080L, "Bélgica", "Senegal", "2026-07-01 15:00:00");
            updateKnockoutMatch(1081L, "Estados Unidos", "Bosnia y Herzegovina", "2026-07-01 19:00:00");
            updateKnockoutMatch(1082L, "España", "Austria", "2026-07-02 14:00:00");
            updateKnockoutMatch(1083L, "Portugal", "Croacia", "2026-07-02 18:00:00");
            updateKnockoutMatch(1084L, "Suiza", "Argelia", "2026-07-02 22:00:00");
            updateKnockoutMatch(1085L, "Australia", "Egipto", "2026-07-03 18:00:00");
            updateKnockoutMatch(1086L, "Argentina", "Cabo Verde", "2026-07-03 22:00:00");
            updateKnockoutMatch(1087L, "Colombia", "Ghana", "2026-07-04 01:30:00");

            // Octavos de final (convertidos de hora local Colombia a UTC sumando 5 horas)
            updateKnockoutMatch(1088L, "Canadá", "Marruecos", "2026-07-04 17:00:00");
            updateKnockoutMatch(1089L, "Paraguay", "Francia", "2026-07-04 21:00:00");
            updateKnockoutMatch(1090L, "Brasil", "Noruega", "2026-07-05 20:00:00");
            updateKnockoutMatch(1091L, "México", "Inglaterra", "2026-07-06 00:00:00");
            updateKnockoutMatch(1092L, "Portugal", "España", "2026-07-06 19:00:00");
            updateKnockoutMatch(1093L, "Estados Unidos", "Bélgica", "2026-07-07 00:00:00");
            updateKnockoutMatch(1094L, "Argentina", "Egipto", "2026-07-07 16:00:00");
            updateKnockoutMatch(1095L, "Suiza", "Colombia", "2026-07-07 20:00:00");

            // Cuartos de final (convertidos de hora local Colombia a UTC sumando 5 horas)
            updateKnockoutMatch(1096L, "Francia", "Marruecos", "2026-07-09 20:00:00");
            updateKnockoutMatch(1097L, "España", "Bélgica", "2026-07-10 19:00:00");
            updateKnockoutMatch(1098L, "Noruega", "Inglaterra", "2026-07-11 21:00:00");
            updateKnockoutMatch(1099L, "Argentina", "Suiza", "2026-07-12 01:00:00");

            // Semifinales
            updateKnockoutMatch(1100L, "Francia", "España", "2026-07-14 19:00:00");
            updateKnockoutMatch(1101L, "Inglaterra", "Argentina", "2026-07-15 19:00:00");

            // Tercer Puesto (Sábado 18 de Julio a las 16:00 de Colombia -> 21:00 UTC)
            updateKnockoutMatch(1102L, null, null, "2026-07-18 21:00:00");

            // Final (Domingo 19 de Julio a las 16:00 de Colombia -> 21:00 UTC)
            updateKnockoutMatch(1103L, null, null, "2026-07-19 21:00:00");

            return "✅ Todos los partidos de fase de grupos y eliminatorias actualizados con los horarios reales de Colombia.";
        } catch (Exception e) {
            return "❌ Error al actualizar horarios reales: " + e.getMessage();
        }
    }

    private void updateMatchTime(String teamA, String teamB, String dateTime) {
        String sql = "UPDATE matches SET match_date = ? WHERE " +
                "(home_team_id = (SELECT id FROM teams WHERE name = ?) AND away_team_id = (SELECT id FROM teams WHERE name = ?)) OR "
                +
                "(home_team_id = (SELECT id FROM teams WHERE name = ?) AND away_team_id = (SELECT id FROM teams WHERE name = ?))";
        jdbcTemplate.update(sql, dateTime, teamA, teamB, teamB, teamA);
    }

    private void updateKnockoutMatch(long id, String teamA, String teamB, String dateTime) {
        String sql = "UPDATE matches SET " +
                "home_team_id = ?, " +
                "away_team_id = ?, " +
                "match_date = ? WHERE id = ?";

        Long homeTeamId = (teamA == null || "A definir".equals(teamA)) ? null
                : jdbcTemplate.queryForObject("SELECT id FROM teams WHERE name = ?", Long.class, teamA);

        Long awayTeamId = (teamB == null || "A definir".equals(teamB)) ? null
                : jdbcTemplate.queryForObject("SELECT id FROM teams WHERE name = ?", Long.class, teamB);

        jdbcTemplate.update(sql, homeTeamId, awayTeamId, dateTime, id);
    }

    @PostMapping("/recalculate-points")
    public String recalculatePoints() {
        try {
            pollaService.recalculateAllUsersTotalPoints();
            return "✅ Todos los puntos de los usuarios han sido recalculados dinámicamente según sus predicciones y el campeón seleccionado.";
        } catch (Exception e) {
            return "❌ Error al recalcular puntos: " + e.getMessage();
        }
    }

    @org.springframework.context.event.EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    public void autoUpdateSchedulesOnStartup() {
        System.out.println("Auto updating match schedules on startup...");
        setRealSchedules();
    }
}
