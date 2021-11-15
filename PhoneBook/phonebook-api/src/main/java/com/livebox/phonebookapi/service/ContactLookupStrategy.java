package com.livebox.phonebookapi.service;

import com.livebox.phonebookapi.model.Contact;

import java.util.List;

public abstract class ContactLookupStrategy {
    public <T> List<Contact> getSearchingParameters(final T param){
        return null;
    };

    public <T> List<Contact> getSearchingParameters(final List<T> param){
        return null;
    };
}
