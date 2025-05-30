// package ru.spb.tksoft.banking.controller;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.constraints.NotNull;
// import lombok.RequiredArgsConstructor;
// import ru.spb.tksoft.banking.dto.user.raw.RawUserDto;
// import ru.spb.tksoft.banking.service.raw.RawUserServiceCached;
// import org.springframework.data.domain.Page;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseStatus;

// /**
//  * User controller.
//  * 
//  * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
//  */
// @RestController
// @RequestMapping(value = "/account")
// @Tag(name = "Account")
// @RequiredArgsConstructor
// public class RawAccountController {

//     @NotNull
//     private final RawAccountServiceCached rawAccountServiceCached;
// }
