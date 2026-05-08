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
            if (teamRepo.count() > 0) {
                System.out.println("Database already contains " + teamRepo.count() + " teams. Skipping seed.");
                return;
            }

            System.out.println("Refreshing database with EXACT 2026 groups...");

            // IMPORTANT: Delete in order to avoid Foreign Key constraint violations
            predictionRepo.deleteAll();
            matchRepo.deleteAll();
            userRepo.deleteAll();
            teamRepo.deleteAll();

            List<Team> teams = new ArrayList<>();

            // --- GRUPO A ---
            teams.add(new Team(null, "México", "🇲🇽", "A"));
            teams.add(new Team(null, "Sudáfrica", "🇿🇦", "A"));
            teams.add(new Team(null, "Corea del Sur", "🇰🇷", "A"));
            teams.add(new Team(null, "Chequia", "🇨🇿", "A"));

            // --- GRUPO B ---
            teams.add(new Team(null, "Canadá", "🇨🇦", "B"));
            teams.add(new Team(null, "Bosnia y Herzegovina", "🇧🇦", "B"));
            teams.add(new Team(null, "Catar", "🇶🇦", "B"));
            teams.add(new Team(null, "Suiza", "🇨🇭", "B"));

            // --- GRUPO C ---
            teams.add(new Team(null, "Brasil", "🇧🇷", "C"));
            teams.add(new Team(null, "Marruecos", "🇲🇦", "C"));
            teams.add(new Team(null, "Haití", "🇭🇹", "C"));
            teams.add(new Team(null, "Escocia", "🏴󠁧󠁢󠁳󠁣󠁴󠁿", "C"));

            // --- GRUPO D ---
            teams.add(new Team(null, "Estados Unidos", "🇺🇸", "D"));
            teams.add(new Team(null, "Paraguay", "🇵🇾", "D"));
            teams.add(new Team(null, "Australia", "🇦🇺", "D"));
            teams.add(new Team(null, "Turquía", "🇹🇷", "D"));

            // --- GRUPO E ---
            teams.add(new Team(null, "Alemania", "🇩🇪", "E"));
            teams.add(new Team(null, "Curazao", "🇨🇼", "E"));
            teams.add(new Team(null, "Costa de Marfil", "🇨🇮", "E"));
            teams.add(new Team(null, "Ecuador", "🇪🇨", "E"));

            // --- GRUPO F ---
            teams.add(new Team(null, "Países Bajos", "🇳🇱", "F"));
            teams.add(new Team(null, "Japón", "🇯🇵", "F"));
            teams.add(new Team(null, "Suecia", "🇸🇪", "F"));
            teams.add(new Team(null, "Túnez", "🇹🇳", "F"));

            // --- GRUPO G ---
            teams.add(new Team(null, "Bélgica", "🇧🇪", "G"));
            teams.add(new Team(null, "Egipto", "🇪🇬", "G"));
            teams.add(new Team(null, "Irán", "🇮🇷", "G"));
            teams.add(new Team(null, "Nueva Zelanda", "🇳🇿", "G"));

            // --- GRUPO H ---
            teams.add(new Team(null, "España", "🇪🇸", "H"));
            teams.add(new Team(null, "Cabo Verde", "🇨🇻", "H"));
            teams.add(new Team(null, "Arabia Saudí", "🇸🇦", "H"));
            teams.add(new Team(null, "Uruguay", "🇺🇾", "H"));

            // --- GRUPO I ---
            teams.add(new Team(null, "Francia", "🇫🇷", "I"));
            teams.add(new Team(null, "Senegal", "🇸🇳", "I"));
            teams.add(new Team(null, "Irak", "🇮🇶", "I"));
            teams.add(new Team(null, "Noruega", "🇳🇴", "I"));

            // --- GRUPO J ---
            teams.add(new Team(null, "Argentina", "🇦🇷", "J"));
            teams.add(new Team(null, "Argelia", "🇩🇿", "J"));
            teams.add(new Team(null, "Austria", "🇦🇹", "J"));
            teams.add(new Team(null, "Jordania", "🇯🇴", "J"));

            // --- GRUPO K ---
            teams.add(new Team(null, "Portugal", "🇵🇹", "K"));
            teams.add(new Team(null, "RD Congo", "🇨🇩", "K"));
            teams.add(new Team(null, "Uzbekistán", "🇺🇿", "K"));
            teams.add(new Team(null, "Colombia", "🇨🇴", "K"));

            // --- GRUPO L ---
            teams.add(new Team(null, "Inglaterra", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", "L"));
            teams.add(new Team(null, "Croacia", "🇭🇷", "L"));
            teams.add(new Team(null, "Ghana", "🇬🇭", "L"));
            teams.add(new Team(null, "Panamá", "🇵🇦", "L"));

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
                            start.plusDays(i), null, null, Match.MatchStatus.SCHEDULED));
                    matchRepo.save(new Match(null, groupTeams.get(2), groupTeams.get(3), 
                            start.plusDays(i).plusHours(4), null, null, Match.MatchStatus.SCHEDULED));
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
