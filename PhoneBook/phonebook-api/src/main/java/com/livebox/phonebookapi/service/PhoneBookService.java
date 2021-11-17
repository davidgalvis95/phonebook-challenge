package com.livebox.phonebookapi.service;

import com.livebox.phonebookapi.model.Contact;
import com.livebox.phonebookapi.model.CreateOrUpdateContactRequest;

import java.sql.SQLDataException;
import java.util.List;
import java.util.UUID;

public interface PhoneBookService {

    List<Contact> searchContactsByCriteria(final List<String> parameters);

    void deleteContact(final UUID id);

    Contact updateContact(final CreateOrUpdateContactRequest contact, final UUID uuid);

    Contact createContact(final CreateOrUpdateContactRequest contact);
}
