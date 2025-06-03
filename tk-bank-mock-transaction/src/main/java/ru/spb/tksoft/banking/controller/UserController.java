package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.UserDto;
import ru.spb.tksoft.banking.service.AuthServiceCached;
import ru.spb.tksoft.banking.service.UserServiceCached;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @NotNull
    private final AuthServiceCached authServiceCached;

    @NotNull
    private final UserServiceCached userServiceCached;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove email from user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/remove/email")
    public UserDto removeEmail(@AuthenticationPrincipal JwtUser user,
            @RequestParam long emailId) {

        return userServiceCached.removeEmail(user, emailId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove phone from user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/remove/phone")
    public UserDto removePhone(@AuthenticationPrincipal JwtUser user,
            @RequestParam long phoneId) {

        return userServiceCached.removePhone(user, phoneId);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update email",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/update/email")
    public UserDto updateEmail(@AuthenticationPrincipal JwtUser user,
            @RequestParam long emailId, @RequestParam String newEmail) {

        return userServiceCached.updateEmail(user, emailId, newEmail);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update phone",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/update/phone")
    public UserDto updatePhone(@AuthenticationPrincipal JwtUser user,
            @RequestParam long phoneId, @RequestParam String newPhone) {

        return userServiceCached.updatePhone(user, phoneId, newPhone);
    }
}
