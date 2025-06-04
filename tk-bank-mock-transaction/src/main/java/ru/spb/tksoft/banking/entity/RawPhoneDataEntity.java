package ru.spb.tksoft.banking.entity;

import java.util.Objects;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User's phone, just data, without relations.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"phone_data\"")
public class RawPhoneDataEntity implements UserContact {

    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User ID. */
    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    /** Phone number. */
    @Column(name = "phone", nullable = false, length = 13, unique = true)
    @Size(max = 13)
    @NotBlank
    private String phone;

    /**
     * Constructor for manual phone creation (adding new phone to the user).
     */
    public RawPhoneDataEntity(final long userId, final String phone) {
        this.userId = userId;
        setContactValue(phone);
    }

    /** UserContact implementation: geting contact's ID. */
    @Override
    public long getContactId() {
        return id;
    }

    private boolean isValidPhone(final String phone) {

        if (phone == null || phone.isBlank()) {
            return false;
        }

        var util = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsed = util.parse(phone, null);
            return util.isValidNumber(parsed);
        } catch (NumberParseException e) {
            return false;
        }
    }

    /** UserContact implementation: setting contact's value. */
    @Override
    public void setContactValue(String contactValue) {

        if (!isValidPhone(contactValue)) {
            throw new IllegalArgumentException("Phone is invalid");
        }
        this.phone = contactValue;
    }

    /** UserContact implementation: getting contact's value. */
    @Override
    public String getContactValue() {
        return phone;
    }

    /** Is current contact equal to the other one? */
    @Override
    public boolean isContactEqual(UserContact other) {
        return Objects.equals(phone, other.getContactValue());
    }
}
