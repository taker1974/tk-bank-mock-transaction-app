package ru.spb.tksoft.banking.entity;

import java.util.Objects;
import org.apache.commons.validator.routines.EmailValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User's email.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"email_data\"")
public class EmailDataEntity implements UserContact {

    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    /** Email. */
    @Column(name = "email", nullable = false, length = 200, unique = true)
    @Size(max = 200)
    @NotBlank
    @Email
    private String email;

    /**
     * Constructor for manual email creation (adding new email to the user).
     */
    public EmailDataEntity(UserEntity user, String email) {
        this.user = user;
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

        if (!isValidEMail(email)) {
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
