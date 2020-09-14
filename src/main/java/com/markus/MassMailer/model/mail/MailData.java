package com.markus.MassMailer.model.mail;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public class MailData implements Serializable {
    public String getBcc;
    private String from;
    private String cc;
    private String bcc;

    public MailData(JSONObject data) throws JSONException {
        this.from = data.getString("from");
        this.cc = data.getString("cc");
        this.bcc = data.getString("bcc");
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public String getFrom() {
        return from;
    }
}
