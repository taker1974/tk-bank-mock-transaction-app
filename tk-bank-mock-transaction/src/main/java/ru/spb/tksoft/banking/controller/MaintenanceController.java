package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.maintenance.MaintenanceInfoDto;
import ru.spb.tksoft.banking.service.AuthServiceCached;
import ru.spb.tksoft.banking.service.RawAccountServiceCached;
import ru.spb.tksoft.banking.service.RawUserServiceCached;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Maintenance controller.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RestController
@RequestMapping(value = "/maintenance")
@Tag(name = "Maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    @NotNull
    private final BuildProperties buildProperties;

    @NotNull
    private final AuthServiceCached authServiceCached;

    @NotNull
    private final RawAccountServiceCached rawAccountServiceCached;

    @NotNull
    private final RawUserServiceCached rawUserServiceCached;

    /**
     * @return Application info.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "About", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/info")
    public MaintenanceInfoDto getInfo() {

        return new MaintenanceInfoDto(
                buildProperties.getName(), buildProperties.getVersion());
    }

    /**
     * Clear caches.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Clear caches", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/clear-caches")
    public void clearCaches() {

        authServiceCached.clearCaches();
        rawAccountServiceCached.clearCaches();
        rawUserServiceCached.clearCaches();
    }
}
