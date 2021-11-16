package com.livebox.phonebookapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
@AllArgsConstructor
public class Contact implements ContactOperationResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private Long phoneNumber;
}
