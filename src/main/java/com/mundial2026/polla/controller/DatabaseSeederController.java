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
    public String seedDatabase() {
        try {
            // Limpiar TODO para asegurar integridad de IDs hardcodeados
            jdbcTemplate.execute("DELETE FROM predictions");
            jdbcTemplate.execute("DELETE FROM matches");
            jdbcTemplate.execute("DELETE FROM teams");
            jdbcTemplate.execute("UPDATE users SET total_points = 0, champion_team_id = null");

            // Re-insertar equipos con IDs fijos (610-657)
            if (true) { // Forzar inserción para asegurar IDs correctos
                // ===== INSERTAR 48 EQUIPOS DEL MUNDIAL 2026 =====
                jdbcTemplate.execute("INSERT INTO teams (id, flag_url, group_name, name) VALUES " +
                        "(610,'mx','A','México')," +
                        "(611,'za','A','Sudáfrica')," +
                        "(612,'kr','A','Corea del Sur')," +
                        "(613,'cz','A','Chequia')," +
                        "(614,'ca','B','Canadá')," +
                        "(615,'ba','B','Bosnia y Herzegovina')," +
                        "(616,'qa','B','Catar')," +
                        "(617,'ch','B','Suiza')," +
                        "(618,'br','C','Brasil')," +
                        "(619,'ma','C','Marruecos')," +
                        "(620,'ht','C','Haití')," +
                        "(621,'gb-sct','C','Escocia')," +
                        "(622,'us','D','Estados Unidos')," +
                        "(623,'py','D','Paraguay')," +
                        "(624,'au','D','Australia')," +
                        "(625,'tr','D','Turquía')," +
                        "(626,'de','E','Alemania')," +
                        "(627,'cw','E','Curazao')," +
                        "(628,'ci','E','Costa de Marfil')," +
                        "(629,'ec','E','Ecuador')," +
                        "(630,'nl','F','Países Bajos')," +
                        "(631,'jp','F','Japón')," +
                        "(632,'se','F','Suecia')," +
                        "(633,'tn','F','Túnez')," +
                        "(634,'be','G','Bélgica')," +
                        "(635,'eg','G','Egipto')," +
                        "(636,'ir','G','Irán')," +
                        "(637,'nz','G','Nueva Zelanda')," +
                        "(638,'es','H','España')," +
                        "(639,'cv','H','Cabo Verde')," +
                        "(640,'sa','H','Arabia Saudí')," +
                        "(641,'uy','H','Uruguay')," +
                        "(642,'fr','I','Francia')," +
                        "(643,'sn','I','Senegal')," +
                        "(644,'iq','I','Irak')," +
                        "(645,'no','I','Noruega')," +
                        "(646,'ar','J','Argentina')," +
                        "(647,'dz','J','Argelia')," +
                        "(648,'at','J','Austria')," +
                        "(649,'jo','J','Jordania')," +
                        "(650,'pt','K','Portugal')," +
                        "(651,'cd','K','RD Congo')," +
                        "(652,'uz','K','Uzbekistán')," +
                        "(653,'co','K','Colombia')," +
                        "(654,'gb-eng','L','Inglaterra')," +
                        "(655,'hr','L','Croacia')," +
                        "(656,'gh','L','Ghana')," +
                        "(657,'pa','L','Panamá')");
            }

            // ===== GENERAR 72 PARTIDOS DE FASE DE GRUPOS (6 por grupo) =====
            String[] groups = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };
            int startTeamId = 610;
            int matchId = 1000;

            for (String g : groups) {
                int t1 = startTeamId;
                int t2 = startTeamId + 1;
                int t3 = startTeamId + 2;
                int t4 = startTeamId + 3;

                // Las 6 combinaciones posibles por grupo
                int[][] matchups = { { t1, t2 }, { t3, t4 }, { t1, t3 }, { t2, t4 }, { t1, t4 }, { t2, t3 } };
                String stageName = "Grupo " + g;

                for (int[] pair : matchups) {
                    jdbcTemplate.update(
                            "INSERT INTO matches (id, home_team_id, away_team_id, match_date, status, stage) VALUES (?, ?, ?, ?, ?, ?)",
                            matchId++, pair[0], pair[1], "2026-06-12 15:00:00", "SCHEDULED", stageName);
                }
                startTeamId += 4;
            }

            // ===== GENERAR FASES FINALES (32 partidos adicionales) =====
            String[] knockoutStages = { "Dieciseisavos", "Octavos", "Cuartos", "Semifinal", "Final" };
            int[] matchesPerStage = { 16, 8, 4, 2, 1 };

            for (int i = 0; i < knockoutStages.length; i++) {
                String stage = knockoutStages[i];
                for (int j = 0; j < matchesPerStage[i]; j++) {
                    jdbcTemplate.update(
                            "INSERT INTO matches (id, home_team_id, away_team_id, match_date, status, stage) VALUES (?, ?, ?, ?, ?, ?)",
                            matchId++, null, null, "2026-07-01 15:00:00", "SCHEDULED", stage);
                }
            }

            // ===== INSERTAR CONFIGURACIONES DEL SISTEMA =====
            jdbcTemplate.execute("INSERT IGNORE INTO system_settings (setting_key, setting_value) VALUES " +
                    "('globalAnnouncement','¡Bienvenidos a la Polla Pro 2026!')," +
                    "('maintenanceMode','false')," +
                    "('pointsChampion','100')," +
                    "('pointsDraw','1')," +
                    "('pointsExact','5')," +
                    "('pointsWinner','3')," +
                    "('registrationOpen','true')");

            return "✅ ¡Mundial 2026 inicializado! 104 partidos generados (72 grupos + 32 eliminatorias).";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
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
            updateKnockoutMatch(1085L, "Australia", "Egipto", "2026-07-03 13:00:00");
            updateKnockoutMatch(1086L, "Argentina", "Cabo Verde", "2026-07-03 17:00:00");
            updateKnockoutMatch(1087L, "Colombia", "Ghana", "2026-07-03 20:30:00");

            return "✅ Todos los partidos de fase de grupos y eliminatorias de 32 actualizados con los horarios reales de Colombia.";
        } catch (Exception e) {
            return "❌ Error al actualizar horarios reales: " + e.getMessage();
        }
    }

    private void updateMatchTime(String teamA, String teamB, String dateTime) {
        String sql = "UPDATE matches SET match_date = ? WHERE " +
                     "(home_team_id = (SELECT id FROM teams WHERE name = ?) AND away_team_id = (SELECT id FROM teams WHERE name = ?)) OR " +
                     "(home_team_id = (SELECT id FROM teams WHERE name = ?) AND away_team_id = (SELECT id FROM teams WHERE name = ?))";
        jdbcTemplate.update(sql, dateTime, teamA, teamB, teamB, teamA);
    }

    private void updateKnockoutMatch(long id, String teamA, String teamB, String dateTime) {
        String sql = "UPDATE matches SET " +
                     "home_team_id = ?, " +
                     "away_team_id = ?, " +
                     "match_date = ? WHERE id = ?";
        
        Long homeTeamId = (teamA == null || "A definir".equals(teamA)) ? null : 
            jdbcTemplate.queryForObject("SELECT id FROM teams WHERE name = ?", Long.class, teamA);
        
        Long awayTeamId = (teamB == null || "A definir".equals(teamB)) ? null : 
            jdbcTemplate.queryForObject("SELECT id FROM teams WHERE name = ?", Long.class, teamB);
            
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
