package ru.spb.tksoft.banking.dto.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "date_of_birth", "password", "balance", "emails", "phones"})
public class UserDto {

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

    /** Current balance. */
    @NotNull
    private BigDecimal balance;

    /** EMail list. */
    @NotNull
    private List<String> emails;

    /** Phone list. */
    @NotNull
    private List<String> phones;
}
