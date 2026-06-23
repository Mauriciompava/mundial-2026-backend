package com.mundial2026.polla.controller;

import com.mundial2026.polla.model.Team;
import com.mundial2026.polla.model.User;
import com.mundial2026.polla.repository.TeamRepository;
import com.mundial2026.polla.repository.UserRepository;
import com.mundial2026.polla.service.PollaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PollaService pollaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users/register")
    public User register(@RequestBody User user) {
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PostMapping("/users/login")
    public User login(@RequestBody User loginData) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(loginData.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Check if password matches (works for both BCrypt and plain text for transition)
        boolean matches = false;
        if (user.getPassword().startsWith("$2a$") || user.getPassword().startsWith("$2b$")) {
            matches = passwordEncoder.matches(loginData.getPassword(), user.getPassword());
        } else {
            // Legacy plain text check
            matches = user.getPassword().equals(loginData.getPassword());
            // Optional: Auto-upgrade password to BCrypt if plain match succeeds
            if (matches) {
                user.setPassword(passwordEncoder.encode(loginData.getPassword()));
                userRepository.save(user);
            }
        }

        if (!matches) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        return user;
    }

    @GetMapping("/teams")
    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    @PostMapping("/users/{userId}/champion")
    public User pickChampion(@PathVariable Long userId, @RequestParam Long teamId) {
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();
        user.setChampionTeam(team);
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        return userRepository.save(user);
    }

    @PostMapping("/users/{userId}/upload-receipt")
    public User uploadReceipt(@PathVariable Long userId, @RequestBody(required = false) String receiptBase64) {
        User user = userRepository.findById(userId).orElseThrow();
        
        if (receiptBase64 == null || receiptBase64.trim().isEmpty() || receiptBase64.equals("\"\"")) {
            user.setPaymentReceipt(null);
        } else {
            String cleanBase64 = receiptBase64;
            if (cleanBase64.startsWith("\"") && cleanBase64.endsWith("\"")) {
                cleanBase64 = cleanBase64.substring(1, cleanBase64.length() - 1);
            }
            user.setPaymentReceipt(cleanBase64);
        }
        return userRepository.save(user);
    }

    @GetMapping("/users/{userId}/receipt")
    public String getReceipt(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getPaymentReceipt() != null ? user.getPaymentReceipt() : "";
    }

    @PostMapping("/users/{userId}/toggle-payment")
    public User togglePayment(@PathVariable Long userId, @RequestParam boolean status) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPaid(status);
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        pollaService.deleteUser(id);
    }
}
