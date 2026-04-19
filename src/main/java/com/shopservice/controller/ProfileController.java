package com.shopservice.controller;

import com.shopservice.dto.ProfileDto;
import com.shopservice.entity.Session;
import com.shopservice.service.AuthService;
import com.shopservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AuthService authService;

    @GetMapping
    public ProfileDto getProfile(
            @RequestHeader("X-Session-Token") String tokenHeader) {
        Session session = authService.requireSession(tokenHeader);
        return profileService.getProfile(session.getUsername());
    }

    @PostMapping
    public void saveProfile(
            @RequestHeader("X-Session-Token") String tokenHeader,
            @RequestBody ProfileDto dto) {
        Session session = authService.requireSession(tokenHeader);
        dto.setUsername(session.getUsername());
        profileService.saveProfile(dto);
    }
}
