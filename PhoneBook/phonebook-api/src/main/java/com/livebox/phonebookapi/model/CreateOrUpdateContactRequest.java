package com.livebox.phonebookapi.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateOrUpdateContactRequest {

    String firstName;

    String lastName;

    Integer phoneNumber;
}
