package com.markus.MassMailer.api;

public class MailReferenceNotFoundException extends RuntimeException {
    MailReferenceNotFoundException(Long id) {
        super("Could not find MailReference " + id);
    }
}
