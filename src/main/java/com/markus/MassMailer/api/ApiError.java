package com.markus.MassMailer.api;

import com.markus.MassMailer.model.mail.ErrorMessage;
import com.markus.MassMailer.model.mail.MailReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiError extends ApiResponse{
    private List<String> errors;

    public ApiError(HttpStatus status, String message, ArrayList<ErrorMessage> errors, MailReference mailReference) {
        super(mailReference);
        this.setStatus(status);
        this.setMessage(message);
        this.errors = errors.stream().map(errorMessage -> errorMessage.toString()).collect(Collectors.toList());;
    }

    public ApiError(HttpStatus status, String message, String error, MailReference mailReference) {
        super(mailReference);
        this.setStatus(status);
        this.setMessage(message);
        errors = Arrays.asList(error);
    }

    public List<String> getErrors() {
        return errors;
    }
}