package com.markus.MassMailer.model.mail;

import com.markus.MassMailer.model.user.User;
import com.markus.MassMailer.service.MailSenderService;
import com.mitchellbosecke.pebble.error.RootAttributeNotFoundException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.Serializable;

public class Mail implements Serializable{
    private User user;
    private MailData mailData;
    private String parsedBody;
    private String parsedSubject;

    public Mail(MailData staticMailData, User user) {
        this.mailData = staticMailData;
        this.user = user;
    }

    public Mail parse(Template bodyTemplate, Template subjectTemplate) throws MailCantGetParsedException {
        try {
            parsedBody = bodyTemplate.parse(user);
            parsedSubject = subjectTemplate.parse(user);
        } catch (RootAttributeNotFoundException | IOException e) {
            throw new MailCantGetParsedException(e);
        }
        return this;
    }

    public boolean send() throws IOException, MessagingException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this);
        // Uncomment to start sending!
        // new MailSenderService().send(this);
        return true;
    }

    @Override
    public String toString() {
        return "\nrecipients=" + user.getMailAddress() +
            "\ncc=" + mailData.getCc() +
            "\nbcc=" + mailData.getBcc() +
            "\nsubject='" + parsedSubject + '\'' +
            "\nbody='" + parsedBody + '\'';
    }

    public MailData getMailData() {
        return mailData;
    }

    public User getUser() {
        return user;
    }

    public String getParsedSubject() {
        return parsedSubject;
    }

    public String getParsedBody() {
        return parsedBody;
    }
}
