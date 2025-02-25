package com.livebox.phonebookapi.repository;

import com.livebox.phonebookapi.model.Contact;

import java.sql.SQLDataException;
import java.util.List;
import java.util.UUID;

public interface PhoneBookRepository {
    Contact createContact(final Contact contact);

    Contact updateContact(final Contact contact, final UUID id);

    void deleteContact(final UUID id);

    List<Contact> findAll();

    List<Contact> filterContactsByMatchingName(final String name);

    List<Contact> filterContactsByMatchingNumber(final Long number);

    List<Contact> filterContactByMatchingMultipleParameters(final Long number, final String firstName, final String lastName);
}
