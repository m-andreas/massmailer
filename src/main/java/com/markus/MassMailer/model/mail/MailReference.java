package com.markus.MassMailer.model.mail;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;


import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class MailReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ArrayList<String> errors;
    private String status;

    protected MailReference(){}

    public MailReference(String status){
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
