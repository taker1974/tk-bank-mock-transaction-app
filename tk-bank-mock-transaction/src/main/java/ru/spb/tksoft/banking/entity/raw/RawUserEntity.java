package ru.spb.tksoft.banking.entity.raw;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User entity, just data, without relations.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class RawUserEntity {

    /** User ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name. */
    @Column(name = "name", nullable = false, length = 500, unique = true)
    @Size(min = 1, max = 500)
    @NotBlank
    private String name;

    /** Password. */
    @Column(name = "password", nullable = false, length = 500)
    @Size(min = 8, max = 500)
    @NotBlank
    private String password;

    /** Birth date. */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
}
