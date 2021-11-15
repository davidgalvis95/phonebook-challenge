package com.livebox.phonebookapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneBookApiResponse {

    private String message;

    private String status;

    private ContactOperationResponse contactResponse;
}
