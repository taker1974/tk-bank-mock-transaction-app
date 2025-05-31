package ru.spb.tksoft.banking.entity;

/**
 * Single contact.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface UserContact {

    /** Get id of contact. */
    long getContactId();

    /** Set value of contact. */
    void setContactValue(String cantactValue);

    /** Get value of contact. */
    String getContactValue();

    /** Is current contact equal to the other one? */
    boolean isContactEqual(UserContact contact);
}
