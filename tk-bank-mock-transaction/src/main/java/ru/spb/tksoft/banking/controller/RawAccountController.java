package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.RawAccountDto;
import ru.spb.tksoft.banking.service.RawAccountService;
import ru.spb.tksoft.banking.service.RawAccountServiceCached;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Account controller for raw accounts.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RestController
@RequestMapping(value = "/account")
@Tag(name = "Account")
@RequiredArgsConstructor
public class RawAccountController {

    @NotNull
    private final RawAccountServiceCached rawAccountServiceCached;

    @NotNull
    private final RawAccountService rawAccountService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all accounts, just data, without relations",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/list")
    public Page<RawAccountDto> getRawAccountList(@AuthenticationPrincipal JwtUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return rawAccountServiceCached.getAllAccounts(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deposit money to account of the given user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/deposit")
    public void deposit(@AuthenticationPrincipal JwtUser user, double amount) {

        long userId = user.userId();
        rawAccountServiceCached.deposit(userId, BigDecimal.valueOf(amount));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Withdraw money from account of the given user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/withdraw")
    public void withdraw(@AuthenticationPrincipal JwtUser user, double amount) {

        long userId = user.userId();
        rawAccountServiceCached.withdraw(userId, BigDecimal.valueOf(amount));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Transfer money from one user's account to another",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/transfer")
    public void transfer(@AuthenticationPrincipal JwtUser user, long userIdTo, double amount) {

        long userId = user.userId();
        rawAccountServiceCached.transfer(userId, userIdTo, BigDecimal.valueOf(amount));
    }
}
