package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.auth.AuthResponseDto;
import ru.spb.tksoft.banking.dto.auth.LoginRequestDto;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Auth controller.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RestController
@RequestMapping(value = "/login")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class AuthController {

    public static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("c63002d3-43bc-52ea-bcf9-d27d96711bc9".getBytes());

    private static final long EXPIRATION_TIME_MS = 86_400_000; // 24 hours

    /**
     * Try login with provided credentials.
     * 
     * @param request login credentials.
     * @return Generated JWT token.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login")
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginRequestDto request) {

        // TODO Do not use java.util.Date!

        // TODO Implement user authentication and token generation.
        long userId = generateUserIdFromEmail(request.getEmail());

        String token = Jwts.builder()
                .claim("USER_ID", userId)
                .subject(request.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SECRET_KEY)
                .compact();

        return new AuthResponseDto(token);
    }

    private long generateUserIdFromEmail(String email) {
        return email.hashCode();
    }
}
