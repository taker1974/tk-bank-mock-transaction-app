package ru.spb.tksoft.banking.dto.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User DTO, just "user" data
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "password", "date_of_birth"})
public class RawUserDto {

    /** Unique user ID. */
    @NotNull
    private long id;

    /** Name. */
    @NotBlank
    private String name;

    /** Password. */
    @NotBlank
    private String password;

    /** Date of birth. */
    @JsonProperty("date_of_birth")
    @NotNull
    private LocalDate birthDate;

    /**
     * Returns user's date of birth in string format.
     * 
     * @return String value.
     */
    public String getBirthDate() {

        return birthDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
