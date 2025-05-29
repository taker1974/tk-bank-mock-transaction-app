package ru.spb.tksoft.banking.entity.raw;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User's account, just data, without relations.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class RawAccountEntity {

    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User ID. */
    @Column(name = "user_id", nullable = false, unique = true)
    @NotNull
    private Long userId;

    /** Balance. */
    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @NotNull
    private BigDecimal balance;

    /** Balance: limit for autoincrement. */
    @Column(name = "balance_autolimit", nullable = false, precision = 15, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @NotNull
    private BigDecimal balanceAutoLimit;
}
