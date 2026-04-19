package com.shopservice.service;

import com.shopservice.dto.ProfileDto;
import com.shopservice.entity.Profile;
import com.shopservice.entity.User;
import com.shopservice.repository.ProfileRepository;
import com.shopservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepo;
    private final UserRepository userRepo;

    public ProfileDto getProfile(String username) {
        return profileRepo.findByUsername(username)
                .map(this::toDto)
                .orElseGet(() -> {
                    // Seed from user registration data if no profile yet
                    User user = userRepo.findByUsername(username).orElse(null);
                    ProfileDto dto = new ProfileDto();
                    dto.setUsername(username);
                    dto.setDisplayName(user != null && user.getDisplayName() != null
                            ? user.getDisplayName() : username);
                    dto.setContactEmail(user != null && user.getEmail() != null
                            ? user.getEmail() : "");
                    dto.setAvatar("");
                    return dto;
                });
    }

    @Transactional
    public void saveProfile(ProfileDto dto) {
        Profile profile = profileRepo.findByUsername(dto.getUsername())
                .orElse(Profile.builder().username(dto.getUsername()).build());
        profile.setDisplayName(dto.getDisplayName());
        profile.setContactEmail(dto.getContactEmail());
        profile.setAvatar(dto.getAvatar());
        profileRepo.save(profile);
    }

    private ProfileDto toDto(Profile p) {
        ProfileDto dto = new ProfileDto();
        dto.setUsername(p.getUsername());
        dto.setDisplayName(p.getDisplayName());
        dto.setContactEmail(p.getContactEmail());
        dto.setAvatar(p.getAvatar());
        return dto;
    }
}
