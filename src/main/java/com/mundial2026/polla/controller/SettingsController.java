package com.mundial2026.polla.controller;

import com.mundial2026.polla.model.SystemSetting;
import com.mundial2026.polla.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private SystemSettingRepository repository;

    @GetMapping
    public Map<String, String> getSettings() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(SystemSetting::getSettingKey, SystemSetting::getSettingValue));
    }

    @PostMapping
    public void saveSettings(@RequestBody Map<String, String> settings) {
        settings.forEach((key, value) -> {
            repository.save(new SystemSetting(key, value));
        });
    }
}
