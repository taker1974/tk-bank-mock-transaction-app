package ru.spb.tksoft.banking.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account DTO, just "account" data
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "user_id", "balance", "balance_autolimit"})
public class RawAccountDto {

    /** Unique account ID. */
    @NotNull
    private long id;

    /** User ID. */
    @JsonProperty("user_id")
    @NotNull
    private long userId;

    /** Balance. */
    @DecimalMin(value = "0.0", inclusive = true)
    @NotNull
    private BigDecimal balance;

    /** Balance: limit for autoincrement. */
    @JsonProperty("balance_autolimit")
    @DecimalMin(value = "0.0", inclusive = true)
    @NotNull
    private BigDecimal balanceAutoLimit;
}
