package com.livebox.phonebookapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class ContactSearchOutput implements ContactOperationResponse {
    private final List<Contact> matchingContacts;
}
