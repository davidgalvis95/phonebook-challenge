package com.livebox.phonebookapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.livebox.phonebookapi.config.serialization.ContactOperationResponseDeserializer;
import com.livebox.phonebookapi.config.serialization.ContactOperationResponseSerializer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneBookApiResponse {

    private String message;

    private String status;

    @JsonSerialize(using = ContactOperationResponseSerializer.class)
    @JsonDeserialize(using = ContactOperationResponseDeserializer.class)
    private ContactOperationResponse response;

    @JsonCreator
    public PhoneBookApiResponse(String message, String status, ContactOperationResponse response) {
        this.message = message;
        this.status = status;
        this.response = response;
    }
}
