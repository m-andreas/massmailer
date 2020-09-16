package com.markus.MassMailer.model.mail;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.stream.Collectors;

@Entity
public class MailReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition="CLOB")
    private String errors;

    public enum Status {
        INITIALIZED,
        PARSED,
        SENDING,
        SENT,
        ERROR_PARSING,
        ERROR_SENDING
    }
    private Status status;

    protected MailReference(){}

    public MailReference(Status status){
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getErrors() {
        return errors;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setErrors(ArrayList<ErrorMessage> errors) {
        this.errors = errors
                        .stream()
                        .map(e -> e.toString())
                        .collect(Collectors.joining(","));
    }
}
