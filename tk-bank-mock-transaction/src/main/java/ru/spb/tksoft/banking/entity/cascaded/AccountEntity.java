package ru.spb.tksoft.banking.entity.cascaded;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User's account.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class AccountEntity {

    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private UserEntity user;

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
