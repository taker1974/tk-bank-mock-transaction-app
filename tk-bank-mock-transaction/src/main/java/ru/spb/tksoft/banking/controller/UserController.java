package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.RawUserDto;
import ru.spb.tksoft.banking.service.RawUserServiceCached;
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
    private final RawUserServiceCached rawUserServiceCached;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users, just data, without relations",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/list")
    public Page<RawUserDto> getRawUserList(@AuthenticationPrincipal JwtUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return rawUserServiceCached.getAllUsers(pageable);
    }
}
