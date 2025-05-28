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
 * User info.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "date_of_birth", "password", "balance", "emails", "phones"})
public class UserInfoDto {

    /** User ID. */
    @NotNull
    private long id;

    /** User's date of birth. */
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

    /** User's password. */
    @NotBlank
    private String password;

    /** Current user's balance. */
    @NotNull
    private BigDecimal balance;

    /** User's email list. */
    @NotNull
    private List<String> emails;

    /** User's phone list. */
    @NotNull
    private List<String> phones;
}
