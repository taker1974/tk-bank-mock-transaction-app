package ru.spb.tksoft.banking.dto;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contact list DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"name", "user_id", "contacts"})
public class RawContactListDto {

    @NotBlank
    private String name;

    /** User ID. */
    @NotNull
    @JsonProperty("user_id")
    private long userId;

    /** Contacts. */
    @NotNull
    private Set<RawContactItemDto> contacts;
}
