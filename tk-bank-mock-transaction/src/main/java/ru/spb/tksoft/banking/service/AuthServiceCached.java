package ru.spb.tksoft.banking.service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.controller.JwtUser;
import ru.spb.tksoft.banking.entity.RawUserEntity;
import ru.spb.tksoft.banking.repository.RawUserRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Cached auth service.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AuthServiceCached {

    private final Logger log = LoggerFactory.getLogger(AuthServiceCached.class);

    /** JWT token key. */
    public static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("c63002d3-43bc-52ea-bcf9-d27d96711bc9".getBytes());

    private static final long EXPIRATION_TIME_MS = 86_400_000; // 24 hours

    @NotNull
    private final RawUserRepository rawRserRepository;

    /** Clear caches. */
    @CacheEvict(value = "token", allEntries = true)
    public void clearCaches() {
        // ...
    }

    /**
     * Get JWT token for user with given credentials.
     * 
     * @return User or empty value.
     */
    @Cacheable(value = "token", unless = "#result.isEmpty()", key = "#email")
    @NotNull
    public Optional<String> getJwtToken(
            @NotNull final String email,
            @NotNull final String password) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        Optional<RawUserEntity> userOptional = rawRserRepository.findOneByEmailExact(email);
        if (userOptional.isEmpty()) {
            LogEx.trace(log, LogEx.getThisMethodName(), "User not found");
            return Optional.empty();
        }

        RawUserEntity user = userOptional.get();

        if (!Objects.equals(password, user.getPassword())) {
            LogEx.trace(log, LogEx.getThisMethodName(), "Wrong password");
            return Optional.empty();
        }

        String token = Jwts.builder()
                .claim("USER_ID", user.getId())
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SECRET_KEY)
                .compact();

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return Optional.of(token);
    }

    /**
     * Check if user with given credentials is valid (just exists).
     * 
     * @param user JwtUser object.
     * @return Validity of user.
     */
    @Cacheable(value = "valid", key = "#user.userId()")
    @NotNull
    public boolean isValidUser(@NotNull final JwtUser user) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        boolean valid = rawRserRepository.findById(user.userId()).isPresent();
        if (!valid) {
            LogEx.warn(log, LogEx.getThisMethodName(),
                    "User with given credentials is invalid");
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return valid;
    }
}
