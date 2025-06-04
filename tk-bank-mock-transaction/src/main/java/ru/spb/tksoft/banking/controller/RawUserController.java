package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.RawContactItemDto;
import ru.spb.tksoft.banking.dto.RawContactListDto;
import ru.spb.tksoft.banking.dto.RawUserDto;
import ru.spb.tksoft.banking.service.RawUserService;
import ru.spb.tksoft.banking.service.RawUserServiceCached;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User controller for raw user, without relations.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RestController
@RequestMapping(value = "/raw-user")
@Tag(name = "Raw user")
@RequiredArgsConstructor
public class RawUserController {

    @NotNull
    private final RawUserService rawUserService;

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
        return rawUserService.getAllUsers(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search users by name% pattern",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/name")
    public Page<RawUserDto> findUsersByNameLike(@AuthenticationPrincipal JwtUser user,
            @RequestParam String namePrefix,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return rawUserService.findUsersByNameLike(namePrefix, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search users by date of birth equal and after",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/age")
    public Page<RawUserDto> findUsersByDateOfBirth(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true, defaultValue = "1900") int year,
            @RequestParam(required = true, defaultValue = "1") int month,
            @RequestParam(required = true, defaultValue = "1") int day,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        LocalDate dateOfBirth = LocalDate.of(year, month, day);
        Pageable pageable = PageRequest.of(page, size);
        return rawUserService.findUsersByDateOfBirth(dateOfBirth, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search user by email exact",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/email")
    public RawUserDto findUsersByEmailExact(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) String email) {

        return rawUserServiceCached.findUserByEmailExact(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search user by phone exact",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search/phone")
    public RawUserDto findUsersByPhoneExact(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) String phone) {

        return rawUserServiceCached.findUserByPhoneExact(phone);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List own emails",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/email/list")
    public RawContactListDto listEmails(@AuthenticationPrincipal JwtUser user) {

        return rawUserServiceCached.getUserEmails(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List own phones",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/phone/list")
    public RawContactListDto listPhones(@AuthenticationPrincipal JwtUser user) {

        return rawUserServiceCached.getUserPhones(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add email to user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/email/add")
    public RawContactItemDto addEmail(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) String email) {

        return rawUserServiceCached.addEmail(user, email);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add phone to user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/phone/add")
    public RawContactItemDto addPhone(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) String phone) {

        return rawUserServiceCached.addPhone(user, phone);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove email from user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/email/remove")
    public void removeEmail(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) long emailId) {

        rawUserServiceCached.removeEmail(user, emailId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove phone from user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/phone/remove")
    public void removePhone(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) long phoneId) {

        rawUserServiceCached.removePhone(user, phoneId);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update email",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/email/update")
    public RawContactItemDto updateEmail(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) long emailId, 
            @RequestParam(required = true) String newEmail) {

        return rawUserServiceCached.updateEmail(user, emailId, newEmail);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update phone",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/phone/update")
    public RawContactItemDto updatePhone(@AuthenticationPrincipal JwtUser user,
            @RequestParam(required = true) long phoneId, 
            @RequestParam(required = true) String newPhone) {

        return rawUserServiceCached.updatePhone(user, phoneId, newPhone);
    }

}
