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
                "(610,'🇲🇽','A','México')," +
                "(611,'🇿🇦','A','Sudáfrica')," +
                "(612,'🇰🇷','A','Corea del Sur')," +
                "(613,'🇨🇿','A','Chequia')," +
                "(614,'🇨🇦','B','Canadá')," +
                "(615,'🇧🇦','B','Bosnia y Herzegovina')," +
                "(616,'🇶🇦','B','Catar')," +
                "(617,'🇨🇭','B','Suiza')," +
                "(618,'🇧🇷','C','Brasil')," +
                "(619,'🇲🇦','C','Marruecos')," +
                "(620,'🇭🇹','C','Haití')," +
                "(621,'🏴\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F','C','Escocia')," +
                "(622,'🇺🇸','D','Estados Unidos')," +
                "(623,'🇵🇾','D','Paraguay')," +
                "(624,'🇦🇺','D','Australia')," +
                "(625,'🇹🇷','D','Turquía')," +
                "(626,'🇩🇪','E','Alemania')," +
                "(627,'🇨🇼','E','Curazao')," +
                "(628,'🇨🇮','E','Costa de Marfil')," +
                "(629,'🇪🇨','E','Ecuador')," +
                "(630,'🇳🇱','F','Países Bajos')," +
                "(631,'🇯🇵','F','Japón')," +
                "(632,'🇸🇪','F','Suecia')," +
                "(633,'🇹🇳','F','Túnez')," +
                "(634,'🇧🇪','G','Bélgica')," +
                "(635,'🇪🇬','G','Egipto')," +
                "(636,'🇮🇷','G','Irán')," +
                "(637,'🇳🇿','G','Nueva Zelanda')," +
                "(638,'🇪🇸','H','España')," +
                "(639,'🇨🇻','H','Cabo Verde')," +
                "(640,'🇸🇦','H','Arabia Saudí')," +
                "(641,'🇺🇾','H','Uruguay')," +
                "(642,'🇫🇷','I','Francia')," +
                "(643,'🇸🇳','I','Senegal')," +
                "(644,'🇮🇶','I','Irak')," +
                "(645,'🇳🇴','I','Noruega')," +
                "(646,'🇦🇷','J','Argentina')," +
                "(647,'🇩🇿','J','Argelia')," +
                "(648,'🇦🇹','J','Austria')," +
                "(649,'🇯🇴','J','Jordania')," +
                "(650,'🇵🇹','K','Portugal')," +
                "(651,'🇨🇩','K','RD Congo')," +
                "(652,'🇺🇿','K','Uzbekistán')," +
                "(653,'🇨🇴','K','Colombia')," +
                "(654,'🏴\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F','L','Inglaterra')," +
                "(655,'🇭🇷','L','Croacia')," +
                "(656,'🇬🇭','L','Ghana')," +
                "(657,'🇵🇦','L','Panamá')");
            }

            // ===== GENERAR 72 PARTIDOS DE FASE DE GRUPOS (6 por grupo) =====
            String[] groups = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
            int startTeamId = 610;
            int matchId = 1000;
            
            for (String g : groups) {
                int t1 = startTeamId;
                int t2 = startTeamId + 1;
                int t3 = startTeamId + 2;
                int t4 = startTeamId + 3;
                
                // Las 6 combinaciones posibles por grupo
                int[][] matchups = { {t1, t2}, {t3, t4}, {t1, t3}, {t2, t4}, {t1, t4}, {t2, t3} };
                String stageName = "Grupo " + g;
                
                for (int[] pair : matchups) {
                    jdbcTemplate.update(
                        "INSERT INTO matches (id, home_team_id, away_team_id, match_date, status, stage) VALUES (?, ?, ?, ?, ?, ?)",
                        matchId++, pair[0], pair[1], "2026-06-12 15:00:00", "SCHEDULED", stageName
                    );
                }
                startTeamId += 4;
            }

            // ===== GENERAR FASES FINALES (32 partidos adicionales) =====
            String[] knockoutStages = {"Dieciseisavos", "Octavos", "Cuartos", "Semifinal", "Final"};
            int[] matchesPerStage = {16, 8, 4, 2, 1};
            
            for (int i = 0; i < knockoutStages.length; i++) {
                String stage = knockoutStages[i];
                for (int j = 0; j < matchesPerStage[i]; j++) {
                    jdbcTemplate.update(
                        "INSERT INTO matches (id, home_team_id, away_team_id, match_date, status, stage) VALUES (?, ?, ?, ?, ?, ?)",
                        matchId++, null, null, "2026-07-01 15:00:00", "SCHEDULED", stage
                    );
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
