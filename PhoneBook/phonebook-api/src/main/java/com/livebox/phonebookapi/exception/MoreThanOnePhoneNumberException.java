package com.livebox.phonebookapi.exception;

public class MoreThanOnePhoneNumberException extends RuntimeException {
    public MoreThanOnePhoneNumberException(final String message) {
        super(message);
    }
}
