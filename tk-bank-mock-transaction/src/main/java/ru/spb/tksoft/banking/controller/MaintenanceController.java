package ru.spb.tksoft.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.maintenance.MaintenanceInfoDto;
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

    private final BuildProperties buildProperties;

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
    @Operation(summary = "Clear caches")
    @PostMapping("/clear-caches")
    public void clearCaches() {

        // ...
    }
}
