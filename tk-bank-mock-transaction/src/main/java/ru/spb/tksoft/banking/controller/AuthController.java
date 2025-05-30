package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.auth.AuthResponseDto;
import ru.spb.tksoft.banking.dto.auth.LoginRequestDto;
import ru.spb.tksoft.banking.service.AuthServiceCached;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Auth controller.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class AuthController {

    @NotNull
    private final AuthServiceCached authServiceCached;

    /**
     * Try login with provided credentials.
     * 
     * @param request login credentials.
     * @return Generated JWT token.
     */
    @Operation(
            summary = "Authentication",
            description = "Getting JWT token by email and password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logged In",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = AuthResponseDto.class)))})
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginRequestDto request) {

        String token = authServiceCached
                .getJwtToken(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        return new AuthResponseDto(token);
    }
}
