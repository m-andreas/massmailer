package com.markus.MassMailer.service;

import com.markus.MassMailer.api.JsonSerializer;
import com.markus.MassMailer.model.mail.MailReference;
import com.markus.MassMailer.model.mail.MassMail;
import com.markus.MassMailer.model.mail.Template;
import com.markus.MassMailer.model.mail.MailData;
import com.markus.MassMailer.model.user.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CreateMailerService {
    String body;
    MailReference mailReference;
    public CreateMailerService(String body, MailReference mailReference){
        this.body = body;
        this.mailReference = mailReference;
    }

    public MassMail createMailer() throws JSONException, IOException {
        JSONObject obj;
        JSONArray usersJSON;
        Template bodyTemplate, subjectTemplate;
        ArrayList<User> users;
        MailData mailData;

        obj = new JSONObject(body);
        bodyTemplate = new Template(obj.getString("body"));
        subjectTemplate = new Template(obj.getString("subject"));
        users = new ArrayList<>();
        usersJSON = obj.getJSONArray("users");
        for (int i = 0; i < usersJSON.length(); i++) {
            users.add( new User(JsonSerializer.toMap(usersJSON.getJSONObject(i))));
        }
        mailData = new MailData(obj.getJSONObject("static_data"));
        return new MassMail(bodyTemplate, subjectTemplate, users, mailData, this.mailReference);
    }
}
