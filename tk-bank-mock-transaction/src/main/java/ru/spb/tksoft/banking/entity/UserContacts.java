package ru.spb.tksoft.banking.entity;

import java.util.List;
import java.util.Optional;

/**
 * Contacts.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface UserContacts {

    /** Get collection of contacts. */
    List<UserContact> getContacts();

    /** Add new contact. */
    void addContact(UserContact contact);

    /** Get contact by id. */
    Optional<UserContact> getContact(long contactId);

    /** Remove contact by id. */
    void removeContact(long contactId);

    /** Update contact. */
    void updateContact(UserContact contact);
}
