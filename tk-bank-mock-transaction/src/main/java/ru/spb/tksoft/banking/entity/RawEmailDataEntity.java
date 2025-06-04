package ru.spb.tksoft.banking.entity;

import java.util.Objects;
import org.apache.commons.validator.routines.EmailValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User's email. Just data, without relations.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"email_data\"")
public class RawEmailDataEntity  implements UserContact {

    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User ID. */
    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    /** Email. */
    @Column(name = "email", nullable = false, length = 200, unique = true)
    @Size(max = 200)
    @NotBlank
    @Email
    private String email;

    /**
     * Constructor for manual email creation (adding new email to the user).
     */
    public RawEmailDataEntity(final long userId, final String email) {
        this.userId = userId;
        setContactValue(email);
    }

    /** UserContact implementation: geting contact's ID. */
    @Override
    public long getContactId() {
        return id;
    }

    private boolean isValidEMail(final String email) {

        return email != null && !email.isBlank() &&
                EmailValidator.getInstance().isValid(email);
    }

    /** UserContact implementation: setting contact's value. */
    @Override
    public void setContactValue(String contactValue) {

        if (!isValidEMail(contactValue)) {
            throw new IllegalArgumentException("Email is invalid");
        }
        this.email = contactValue;
    }

    /** UserContact implementation: getting contact's value. */
    @Override
    public String getContactValue() {
        return email;
    }

    /** Is current contact equal to the other one? */
    @Override
    public boolean isContactEqual(UserContact other) {
        return Objects.equals(email, other.getContactValue());
    }
}
