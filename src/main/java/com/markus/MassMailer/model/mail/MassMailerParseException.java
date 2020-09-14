package com.markus.MassMailer.model.mail;

import java.util.ArrayList;

public class MassMailerParseException extends RuntimeException {
    public MassMailerParseException(ArrayList<ErrorMessage> errors) {
        super();
    }
}
