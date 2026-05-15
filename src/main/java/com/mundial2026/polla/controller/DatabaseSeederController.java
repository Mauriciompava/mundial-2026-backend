package com.mundial2026.polla.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class DatabaseSeederController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/seed")
    public String seedDatabase() {
        try {
            // Limpiar TODO para asegurar integridad de IDs hardcodeados
            jdbcTemplate.execute("DELETE FROM predictions");
            jdbcTemplate.execute("DELETE FROM matches");
            jdbcTemplate.execute("DELETE FROM teams");

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
}
