package com.mundial2026.polla.controller;

import com.mundial2026.polla.model.Team;
import com.mundial2026.polla.model.User;
import com.mundial2026.polla.repository.TeamRepository;
import com.mundial2026.polla.repository.UserRepository;
import com.mundial2026.polla.service.PollaService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/users/login")
    public User login(@RequestBody User loginData) {
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(loginData.getEmail()) && u.getPassword().equals(loginData.getPassword()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
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
    public User uploadReceipt(@PathVariable Long userId, @RequestBody String receiptBase64) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPaymentReceipt(receiptBase64);
        return userRepository.save(user);
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
