package com.markus.MassMailer.service;

import com.markus.MassMailer.model.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class MailSenderService {
    public void send(Mail mail) throws IOException, MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("host20.ssl-gesichert.at");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("java@web.wien");
        javaMailSender.setPassword("javarocks");

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(mail.getMailData().getFrom());
        helper.setTo(mail.getUser().getMailAddress());
        helper.setCc(mail.getMailData().getCc());
        helper.setBcc(mail.getMailData().getBcc);
        helper.setSubject(mail.getParsedSubject());
        helper.setText(mail.getParsedBody(), true);

        javaMailSender.send(message);

    }
}
