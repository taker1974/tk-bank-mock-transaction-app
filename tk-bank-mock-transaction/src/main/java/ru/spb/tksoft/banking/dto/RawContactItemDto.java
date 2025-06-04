package ru.spb.tksoft.banking.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contact item DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "contact"})
public class RawContactItemDto {

    /** Unique own ID. */
    @NotNull
    private long id;

    /** Contact. */
    @NotNull
    private String contact;
}
