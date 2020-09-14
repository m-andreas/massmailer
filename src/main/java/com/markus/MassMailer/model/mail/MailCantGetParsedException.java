package com.markus.MassMailer.model.mail;

public class MailCantGetParsedException extends RuntimeException {
    public MailCantGetParsedException(Exception e) {
        super(e);
    }
}

