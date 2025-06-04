package ru.spb.tksoft.banking.entity;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import jakarta.validation.constraints.NotNull;

/**
 * User contacts implementation.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class UserContactsImpl implements UserContacts {

    @NotNull
    private final Set<UserContact> contacts;

    private void checkNotNull(UserContact contact) {

        if (contact == null) {
            throw new IllegalArgumentException("Contact object must not be null");
        }
    }

    /**
     * Constructor.
     */
    @SuppressWarnings("unchecked")
    public UserContactsImpl(Set<? extends UserContact> contacts) {

        if (contacts == null) {
            throw new IllegalArgumentException("Contacts set must not be null");
        }

        this.contacts = (Set<UserContact>) (contacts);
    }

    /**
     * Get collection of contacts.
     * 
     * @return Collection of contacts or empty list.
     */
    @Override
    @NotNull
    public Set<UserContact> getContacts() {
        return Collections
                .unmodifiableSet(this.contacts != null ? this.contacts : Collections.emptySet());
    }

    /**
     * Add new contact.
     * 
     * @param contact New validated contact.
     * @throws IllegalArgumentException if contact is null or with same value already exists.
     */
    @Override
    public void addContact(final UserContact contact) {

        checkNotNull(contact);
        if (contacts.stream().anyMatch(c -> c.isContactEqual(contact))) {
            throw new IllegalArgumentException("Same contact exists");
        }

        contacts.add(contact);
    }

    /**
     * Get contact by id.
     * 
     * 
     * @param contactId Contact ID.
     * @return UserContact or empty value.
     */
    @Override
    public Optional<UserContact> getContact(long contactId) {
        return contacts.stream()
                .filter(c -> c.getContactId() == contactId).findFirst();
    }

    /**
     * Remove contact by ID.
     * 
     * @param contactId Contact ID.
     * @throws IllegalArgumentException if contact ID not found.
     */
    @Override
    public void removeContact(long contactId) {

        if (contacts.stream().noneMatch(c -> c.getContactId() == contactId)) {
            throw new IllegalArgumentException("Contact ID not found");
        }
        contacts.removeIf(c -> c.getContactId() == contactId);
    }

    private UserContact getContactById(long contactId) {
        return contacts.stream()
                .filter(c -> c.getContactId() == contactId).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Contact ID not found"));
    }


    /**
     * Update contact.
     * 
     * @param contactUpdated Updated validated contact.
     * @throws IllegalArgumentException if contact ID not found or updated contact with same value
     *         already exists.
     */
    @Override
    public void updateContact(final UserContact contactUpdated) {

        checkNotNull(contactUpdated);
        UserContact contact = getContactById(contactUpdated.getContactId());

        contact.setContactValue(contactUpdated.getContactValue());
    }
}
