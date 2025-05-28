package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.user.UserInfoDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User controller.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RestController
@RequestMapping(value = "/user")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserController {

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user info", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/info")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal JwtUser user) {

        // TODO Get real user info from DB

        return new UserInfoDto(123, LocalDate.of(1974, 4, 16), "12345678",
                BigDecimal.valueOf(1234.56),
                Arrays.asList("vasya@basya.ru", "vasya@pupkin.ru"), Arrays.asList("+71234567890"));
    }
}
