package com.shopservice.controller;

import com.shopservice.dto.*;
import com.shopservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register, login, logout and session endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user",
               description = "Creates a new user account and seeds an empty profile. Returns 409 if username already exists.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "409", description = "Username already taken")
    })
    public RegisterResponse register(@RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    @Operation(summary = "Login or sync an existing session",
               description = "Normal login validates credentials and returns a new token. " +
                             "Dual-mode sync: include a `token` field to store a pre-generated Node.js session.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public LoginResponse login(@RequestBody LoginRequest req) {
        return authService.loginOrSyncSession(req);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout — invalidate session",
               description = "Deletes the session and clears associated cart and shipping info.")
    @SecurityRequirement(name = "SessionToken")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    public void logout(
            @Parameter(description = "Session token", required = true)
            @RequestHeader(value = "X-Session-Token", required = false) String token) {
        if (token != null && !token.isBlank()) {
            authService.logout(token);
        }
    }

    @GetMapping("/session")
    @Operation(summary = "Get current session metadata",
               description = "Returns username, createdAt, expiresAt, rememberMe and a token preview.")
    @SecurityRequirement(name = "SessionToken")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session info returned"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired session token")
    })
    public SessionResponse session(
            @Parameter(description = "Session token", required = true)
            @RequestHeader(value = "X-Session-Token", required = false) String token) {
        return authService.getSessionInfo(token);
    }
}
