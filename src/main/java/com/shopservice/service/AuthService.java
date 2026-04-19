package com.shopservice.service;

import com.shopservice.dto.*;
import com.shopservice.entity.Session;
import com.shopservice.entity.User;
import com.shopservice.exception.ApiException;
import com.shopservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final SessionRepository sessionRepo;
    private final CartRepository cartRepo;
    private final ShippingInfoRepository shippingRepo;

    // ── Register ────────────────────────────────────────────────────────────

    @Transactional
    public RegisterResponse register(RegisterRequest req) {
        if (req.getUsername() == null || req.getUsername().length() < 3)
            throw new ApiException(400, "Username must be at least 3 characters.");
        if (req.getPassword() == null || req.getPassword().length() < 6)
            throw new ApiException(400, "Password must be at least 6 characters.");
        if (userRepo.existsByUsername(req.getUsername()))
            throw new ApiException(409, "Username already taken.");

        User user = User.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .email(req.getEmail())
                .displayName(req.getDisplayName())
                .build();
        userRepo.save(user);
        return new RegisterResponse(user.getUsername(), user.getEmail(), user.getDisplayName());
    }

    // ── Login / session sync ────────────────────────────────────────────────

    @Transactional
    public LoginResponse loginOrSyncSession(LoginRequest req) {
        if (req.getToken() != null && !req.getToken().isBlank()) {
            // Session-sync from Node.js dual-mode: store the pre-generated session
            sessionRepo.findByToken(req.getToken()).ifPresent(sessionRepo::delete);
            Session session = Session.builder()
                    .token(req.getToken())
                    .username(req.getUsername())
                    .createdAt(req.getCreatedAt())
                    .expiresAt(req.getExpiresAt())
                    .rememberMe(Boolean.TRUE.equals(req.getRememberMe()))
                    .build();
            sessionRepo.save(session);
            return new LoginResponse(session.getToken(), session.getUsername(),
                    session.getExpiresAt(), session.getRememberMe());
        }

        // Normal login: validate credentials and create a new session
        User user = userRepo.findByUsername(req.getUsername())
                .filter(u -> u.getPassword().equals(req.getPassword()))
                .orElseThrow(() -> new ApiException(401, "Invalid username or password."));

        boolean rememberMe = Boolean.TRUE.equals(req.getRememberMe());
        Instant now = Instant.now();
        Instant expiry = rememberMe
                ? now.plus(30, ChronoUnit.DAYS)
                : now.plus(1, ChronoUnit.HOURS);

        byte[] bytes = new byte[8];
        new SecureRandom().nextBytes(bytes);
        String token = "svc-token-" + HexFormat.of().formatHex(bytes);

        Session session = Session.builder()
                .token(token)
                .username(user.getUsername())
                .createdAt(now.toString())
                .expiresAt(expiry.toString())
                .rememberMe(rememberMe)
                .build();
        sessionRepo.save(session);
        return new LoginResponse(token, user.getUsername(), expiry.toString(), rememberMe);
    }

    // ── Logout ──────────────────────────────────────────────────────────────

    @Transactional
    public void logout(String token) {
        sessionRepo.deleteByToken(token);
        cartRepo.deleteBySessionToken(token);
        shippingRepo.deleteBySessionToken(token);
    }

    // ── Session validation (used by all protected controllers) ──────────────

    public Session requireSession(String token) {
        if (token == null || token.isBlank())
            throw new ApiException(401, "Unauthorized");
        Session session = sessionRepo.findByToken(token)
                .orElseThrow(() -> new ApiException(401, "Unauthorized"));
        if (session.getExpiresAt() != null) {
            Instant expiry = Instant.parse(session.getExpiresAt());
            if (Instant.now().isAfter(expiry)) {
                sessionRepo.delete(session);
                throw new ApiException(401, "Session expired. Please log in again.");
            }
        }
        return session;
    }

    public SessionResponse getSessionInfo(String token) {
        Session s = requireSession(token);
        String preview = s.getToken().length() > 18
                ? s.getToken().substring(0, 18) + "…"
                : s.getToken();
        return new SessionResponse(s.getUsername(), s.getCreatedAt(),
                s.getExpiresAt(), s.getRememberMe(), preview);
    }
}
