package com.shopservice.controller;

import com.shopservice.dto.SettingsDto;
import com.shopservice.entity.Session;
import com.shopservice.service.AuthService;
import com.shopservice.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;
    private final AuthService authService;

    @GetMapping
    public SettingsDto getSettings(
            @RequestHeader("X-Session-Token") String tokenHeader) {
        Session session = authService.requireSession(tokenHeader);
        return settingsService.getSettings(session.getUsername());
    }

    @PostMapping
    public void saveSettings(
            @RequestHeader("X-Session-Token") String tokenHeader,
            @RequestBody SettingsDto dto) {
        Session session = authService.requireSession(tokenHeader);
        dto.setUsername(session.getUsername());
        settingsService.saveSettings(dto);
    }
}
