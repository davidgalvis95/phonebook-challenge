package com.livebox.phonebookapi.controller;

import com.livebox.phonebookapi.model.*;
import com.livebox.phonebookapi.service.PhoneBookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping(PhoneBookApiPaths.PHONEBOOK_API_BASE_PATH)
@RestController
@AllArgsConstructor
public class PhoneBookController {

    private final PhoneBookService phoneBookService;

    @GetMapping(PhoneBookApiPaths.PHONEBOOK_API_READER_PATH)
    public ResponseEntity<PhoneBookApiResponse> searchByCriteria(@RequestParam(value = "searchingParams", required = false) final List<String> params) {
        final List<Contact> matchedContacts;
        if (params == null || params.isEmpty()) {
            matchedContacts = phoneBookService.searchContactsByCriteria(Collections.emptyList());

        } else {
            matchedContacts = phoneBookService.searchContactsByCriteria(params);

        }

        if (matchedContacts.isEmpty()) {
            return ResponseEntity.of(Optional.of(PhoneBookApiResponse.builder()
                    .message("No contacts match the searching criteria")
                    .status(HttpStatus.NOT_FOUND.toString())
                    .build()));
        }

        return ResponseEntity.ok(PhoneBookApiResponse.builder()
                .message("Results processed successfully")
                .status(HttpStatus.OK.toString())
                .response(ContactSearchOutput.builder()
                        .matchingContacts(matchedContacts)
                        .build())
                .build());
    }

    @PostMapping(PhoneBookApiPaths.PHONEBOOK_API_UPDATE_PATH)
    public ResponseEntity<PhoneBookApiResponse> createContact(@RequestBody final CreateOrUpdateContactRequest contactRequest) {
        try {
            return ResponseEntity.ok(PhoneBookApiResponse.builder()
                    .message("Contact created")
                    .status(HttpStatus.OK.toString())
                    .response(phoneBookService.createContact(contactRequest))
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.of(Optional.of(PhoneBookApiResponse.builder()
                    .message("The contact could not be created due to some server failures")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .build()));
        }
    }

    @PutMapping(PhoneBookApiPaths.PHONEBOOK_API_UPDATE_PATH)
    public ResponseEntity<PhoneBookApiResponse> updateContact(@RequestParam(value = "id") final UUID id,
                                                              @RequestBody final CreateOrUpdateContactRequest contactRequest) {
        try {
            return ResponseEntity.ok(PhoneBookApiResponse.builder()
                    .message(String.format("Contact with id '%s' was updated successfully", id.toString()))
                    .status(HttpStatus.OK.toString())
                    .response(phoneBookService.updateContact(contactRequest, id))
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.of(Optional.of(PhoneBookApiResponse.builder()
                    .message(String.format("Contact with id '%s' could not be updated due to server failures, please try again", id.toString()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .build()));
        }
    }

    @DeleteMapping(PhoneBookApiPaths.PHONEBOOK_API_UPDATE_PATH)
    public ResponseEntity<PhoneBookApiResponse> deleteContact(@RequestParam(value = "id") final UUID id) {
        try {
            phoneBookService.deleteContact(id);
            return ResponseEntity.ok(PhoneBookApiResponse.builder()
                    .message(String.format("Contact with id '%s' has been removed from records", id.toString()))
                    .status(HttpStatus.OK.toString())
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.of(Optional.of(PhoneBookApiResponse.builder()
                    .message(String.format("Contact with id '%s' could not be removed from records due to server failures, please try again", id.toString()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .build()));
        }
    }

}
