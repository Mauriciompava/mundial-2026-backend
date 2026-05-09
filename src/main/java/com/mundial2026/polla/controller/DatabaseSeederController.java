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
            // Verificar si ya hay datos
            Integer teamCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM teams", Integer.class);
            if (teamCount != null && teamCount > 0) {
                return "La base de datos ya contiene " + teamCount + " equipos y está lista.";
            }

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

            // ===== INSERTAR 24 PARTIDOS INAUGURALES =====
            jdbcTemplate.execute("INSERT INTO matches (id, away_score, home_score, match_date, status, away_team_id, home_team_id) VALUES " +
                "(222,NULL,NULL,'2026-06-11 20:00:00.000000','SCHEDULED',611,610)," +
                "(223,NULL,NULL,'2026-06-12 00:00:00.000000','SCHEDULED',613,612)," +
                "(224,NULL,NULL,'2026-06-12 20:00:00.000000','SCHEDULED',615,614)," +
                "(225,NULL,NULL,'2026-06-13 00:00:00.000000','SCHEDULED',617,616)," +
                "(226,NULL,NULL,'2026-06-13 20:00:00.000000','SCHEDULED',619,618)," +
                "(227,NULL,NULL,'2026-06-14 00:00:00.000000','SCHEDULED',621,620)," +
                "(228,NULL,NULL,'2026-06-14 20:00:00.000000','SCHEDULED',623,622)," +
                "(229,NULL,NULL,'2026-06-15 00:00:00.000000','SCHEDULED',625,624)," +
                "(230,NULL,NULL,'2026-06-15 20:00:00.000000','SCHEDULED',627,626)," +
                "(231,NULL,NULL,'2026-06-16 00:00:00.000000','SCHEDULED',629,628)," +
                "(232,NULL,NULL,'2026-06-16 20:00:00.000000','SCHEDULED',631,630)," +
                "(233,NULL,NULL,'2026-06-17 00:00:00.000000','SCHEDULED',633,632)," +
                "(234,NULL,NULL,'2026-06-17 20:00:00.000000','SCHEDULED',635,634)," +
                "(235,NULL,NULL,'2026-06-18 00:00:00.000000','SCHEDULED',637,636)," +
                "(236,NULL,NULL,'2026-06-18 20:00:00.000000','SCHEDULED',639,638)," +
                "(237,NULL,NULL,'2026-06-19 00:00:00.000000','SCHEDULED',641,640)," +
                "(238,NULL,NULL,'2026-06-19 20:00:00.000000','SCHEDULED',643,642)," +
                "(239,NULL,NULL,'2026-06-20 00:00:00.000000','SCHEDULED',645,644)," +
                "(240,NULL,NULL,'2026-06-20 20:00:00.000000','SCHEDULED',647,646)," +
                "(241,NULL,NULL,'2026-06-21 00:00:00.000000','SCHEDULED',649,648)," +
                "(242,NULL,NULL,'2026-06-21 20:00:00.000000','SCHEDULED',651,650)," +
                "(243,NULL,NULL,'2026-06-22 00:00:00.000000','SCHEDULED',653,652)," +
                "(244,NULL,NULL,'2026-06-22 20:00:00.000000','SCHEDULED',655,654)," +
                "(245,NULL,NULL,'2026-06-23 00:00:00.000000','SCHEDULED',657,656)");

            // ===== INSERTAR CONFIGURACIONES DEL SISTEMA =====
            jdbcTemplate.execute("INSERT IGNORE INTO system_settings (setting_key, setting_value) VALUES " +
                "('globalAnnouncement','¡Bienvenidos a la Polla Pro 2026!')," +
                "('maintenanceMode','false')," +
                "('pointsChampion','100')," +
                "('pointsDraw','1')," +
                "('pointsExact','5')," +
                "('pointsWinner','3')," +
                "('registrationOpen','true')");

            return "✅ ¡Mundial 2026 inicializado! 48 equipos + 24 partidos + reglas cargadas.";

        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}
