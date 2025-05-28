package ru.spb.tksoft.banking.dto.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login request.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"email", "password"})
public class LoginRequestDto {

    /** E-Mail.*/
    @NotBlank
    private String email;

    /** Password.*/
    @NotBlank
    private String password;
}
