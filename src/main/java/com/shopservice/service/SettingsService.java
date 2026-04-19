package com.shopservice.service;

import com.shopservice.dto.SettingsDto;
import com.shopservice.entity.UserSettings;
import com.shopservice.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserSettingsRepository settingsRepo;

    public SettingsDto getSettings(String username) {
        return settingsRepo.findByUsername(username)
                .map(this::toDto)
                .orElseGet(() -> {
                    SettingsDto dto = new SettingsDto();
                    dto.setUsername(username);
                    dto.setEmailNotifications(true);
                    dto.setSmsNotifications(false);
                    dto.setTheme("light");
                    dto.setCurrency("USD");
                    dto.setItemsPerPage(4);
                    return dto;
                });
    }

    @Transactional
    public void saveSettings(SettingsDto dto) {
        UserSettings settings = settingsRepo.findByUsername(dto.getUsername())
                .orElse(UserSettings.builder().username(dto.getUsername()).build());
        if (dto.getEmailNotifications() != null) settings.setEmailNotifications(dto.getEmailNotifications());
        if (dto.getSmsNotifications() != null)  settings.setSmsNotifications(dto.getSmsNotifications());
        if (dto.getTheme() != null)             settings.setTheme(dto.getTheme());
        if (dto.getCurrency() != null)          settings.setCurrency(dto.getCurrency());
        if (dto.getItemsPerPage() != null)      settings.setItemsPerPage(dto.getItemsPerPage());
        settingsRepo.save(settings);
    }

    private SettingsDto toDto(UserSettings s) {
        SettingsDto dto = new SettingsDto();
        dto.setUsername(s.getUsername());
        dto.setEmailNotifications(s.getEmailNotifications());
        dto.setSmsNotifications(s.getSmsNotifications());
        dto.setTheme(s.getTheme());
        dto.setCurrency(s.getCurrency());
        dto.setItemsPerPage(s.getItemsPerPage());
        return dto;
    }
}
