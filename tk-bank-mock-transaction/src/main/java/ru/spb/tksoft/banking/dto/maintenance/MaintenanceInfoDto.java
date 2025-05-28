package ru.spb.tksoft.banking.dto.maintenance;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Application info.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"name", "version"})
public class MaintenanceInfoDto {

    /** Application ID.*/
    @NotBlank
    private String name;

    /** Version.*/
    @NotBlank
    private String version;
}
