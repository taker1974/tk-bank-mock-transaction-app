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

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/list")
    public Page<UserDto> findAllUsers(@AuthenticationPrincipal JwtUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return userServiceCached.findAllUsers(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search users by name% pattern",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/name")
    public Page<UserDto> findUsersByNameLike(@AuthenticationPrincipal JwtUser user,
            @RequestParam String namePrefix,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return userServiceCached.findUsersByNameLike(namePrefix, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search users by date of birth equal and after",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/age")
    public Page<UserDto> findUsersByDateOfBirth(@AuthenticationPrincipal JwtUser user,
            @RequestParam(defaultValue = "1900") int year,
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "1") int day,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        LocalDate dateOfBirth = LocalDate.of(year, month, day);
        Pageable pageable = PageRequest.of(page, size);
        return userServiceCached.findUsersByDateOfBirth(dateOfBirth, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search user by email exact",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/email")
    public UserDto findUsersByEmailExact(@AuthenticationPrincipal JwtUser user,
            @RequestParam String email) {

        return userServiceCached.findUsersByEmailExact(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search user by phone exact",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/phone")
    public UserDto findUsersByPhoneExact(@AuthenticationPrincipal JwtUser user,
            @RequestParam String phone) {

        return userServiceCached.findUsersByPhoneExact(phone);
    }
}
