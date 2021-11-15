package com.livebox.phonebookapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ContactSearchOutput implements ContactOperationResponse {
    private final List<Contact> matchingContacts;
}
