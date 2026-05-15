package com.mundial2026.polla.config;

import com.mundial2026.polla.model.Match;
import com.mundial2026.polla.model.Team;
import com.mundial2026.polla.model.User;
import com.mundial2026.polla.repository.MatchRepository;
import com.mundial2026.polla.repository.PredictionRepository;
import com.mundial2026.polla.repository.TeamRepository;
import com.mundial2026.polla.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            TeamRepository teamRepo,
            MatchRepository matchRepo,
            UserRepository userRepo,
            PredictionRepository predictionRepo) {
        return args -> {
            // Check if data already exists to avoid redundant deletions
            // Detect if we need to migrate from emojis to ISO codes
            boolean needsMigration = teamRepo.findAll().stream()
                    .anyMatch(t -> t.getName().equals("México") && t.getFlagUrl().equals("🇲🇽"));

            if (teamRepo.count() > 0 && !needsMigration) {
                System.out.println("Database already contains " + teamRepo.count() + " teams with correct flags. Skipping seed.");
                return;
            }

            if (needsMigration) {
                System.out.println("Legacy emoji flags detected. FORCING DATABASE RE-SEED...");
            }

            System.out.println("Refreshing database with EXACT 2026 groups...");

            // IMPORTANT: Delete in order to avoid Foreign Key constraint violations
            predictionRepo.deleteAll();
            matchRepo.deleteAll();
            userRepo.deleteAll();
            teamRepo.deleteAll();

            List<Team> teams = new ArrayList<>();

            // --- GRUPO A ---
            teams.add(new Team(null, "México", "mx", "A"));
            teams.add(new Team(null, "Sudáfrica", "za", "A"));
            teams.add(new Team(null, "Corea del Sur", "kr", "A"));
            teams.add(new Team(null, "Chequia", "cz", "A"));

            // --- GRUPO B ---
            teams.add(new Team(null, "Canadá", "ca", "B"));
            teams.add(new Team(null, "Bosnia y Herzegovina", "ba", "B"));
            teams.add(new Team(null, "Catar", "qa", "B"));
            teams.add(new Team(null, "Suiza", "ch", "B"));

            // --- GRUPO C ---
            teams.add(new Team(null, "Brasil", "br", "C"));
            teams.add(new Team(null, "Marruecos", "ma", "C"));
            teams.add(new Team(null, "Haití", "ht", "C"));
            teams.add(new Team(null, "Escocia", "gb-sct", "C"));

            // --- GRUPO D ---
            teams.add(new Team(null, "Estados Unidos", "us", "D"));
            teams.add(new Team(null, "Paraguay", "py", "D"));
            teams.add(new Team(null, "Australia", "au", "D"));
            teams.add(new Team(null, "Turquía", "tr", "D"));

            // --- GRUPO E ---
            teams.add(new Team(null, "Alemania", "de", "E"));
            teams.add(new Team(null, "Curazao", "cw", "E"));
            teams.add(new Team(null, "Costa de Marfil", "ci", "E"));
            teams.add(new Team(null, "Ecuador", "ec", "E"));

            // --- GRUPO F ---
            teams.add(new Team(null, "Países Bajos", "nl", "F"));
            teams.add(new Team(null, "Japón", "jp", "F"));
            teams.add(new Team(null, "Suecia", "se", "F"));
            teams.add(new Team(null, "Túnez", "tn", "F"));

            // --- GRUPO G ---
            teams.add(new Team(null, "Bélgica", "be", "G"));
            teams.add(new Team(null, "Egipto", "eg", "G"));
            teams.add(new Team(null, "Irán", "ir", "G"));
            teams.add(new Team(null, "Nueva Zelanda", "nz", "G"));

            // --- GRUPO H ---
            teams.add(new Team(null, "España", "es", "H"));
            teams.add(new Team(null, "Cabo Verde", "cv", "H"));
            teams.add(new Team(null, "Arabia Saudí", "sa", "H"));
            teams.add(new Team(null, "Uruguay", "uy", "H"));

            // --- GRUPO I ---
            teams.add(new Team(null, "Francia", "fr", "I"));
            teams.add(new Team(null, "Senegal", "sn", "I"));
            teams.add(new Team(null, "Irak", "iq", "I"));
            teams.add(new Team(null, "Noruega", "no", "I"));

            // --- GRUPO J ---
            teams.add(new Team(null, "Argentina", "ar", "J"));
            teams.add(new Team(null, "Argelia", "dz", "J"));
            teams.add(new Team(null, "Austria", "at", "J"));
            teams.add(new Team(null, "Jordania", "jo", "J"));

            // --- GRUPO K ---
            teams.add(new Team(null, "Portugal", "pt", "K"));
            teams.add(new Team(null, "RD Congo", "cd", "K"));
            teams.add(new Team(null, "Uzbekistán", "uz", "K"));
            teams.add(new Team(null, "Colombia", "co", "K"));

            // --- GRUPO L ---
            teams.add(new Team(null, "Inglaterra", "gb-eng", "L"));
            teams.add(new Team(null, "Croacia", "hr", "L"));
            teams.add(new Team(null, "Ghana", "gh", "L"));
            teams.add(new Team(null, "Panamá", "pa", "L"));

            List<Team> savedTeams = teamRepo.saveAll(teams);

            // SEED MATCHES (Group Stage)
            LocalDateTime start = LocalDateTime.of(2026, 6, 11, 15, 0);

            // Seed Matches for ALL Groups A-L based on the new assignments
            String[] groupNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
            for (int i = 0; i < groupNames.length; i++) {
                String group = groupNames[i];
                List<Team> groupTeams = savedTeams.stream()
                        .filter(t -> t.getGroupName().equals(group))
                        .toList();
                
                if (groupTeams.size() >= 4) {
                    // Jornada 1 del Grupo
                    matchRepo.save(new Match(null, groupTeams.get(0), groupTeams.get(1), 
                            start.plusDays(i), null, null, "Grupo " + group, Match.MatchStatus.SCHEDULED));
                    matchRepo.save(new Match(null, groupTeams.get(2), groupTeams.get(3), 
                            start.plusDays(i).plusHours(4), null, null, "Grupo " + group, Match.MatchStatus.SCHEDULED));
                }
            }

            // Seed User
            userRepo.save(new User(null, "Mateo", "mateo@test.com", "123", 1500, 20000.0, findTeam(savedTeams, "Colombia")));

            System.out.println("BACKEND READY: Exact World Cup 2026 groups and teams loaded.");
        };
    }

    private Team findTeam(List<Team> teams, String name) {
        return teams.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }
}
