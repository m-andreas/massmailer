package com.markus.MassMailer.model.mail;

import com.google.common.collect.Lists;
import com.markus.MassMailer.api.ApiError;
import com.markus.MassMailer.model.data.MailReferenceRepository;
import com.markus.MassMailer.model.user.User;
import com.markus.MassMailer.service.FileCleanUpService;
import com.markus.MassMailer.service.FileReaderService;
import com.markus.MassMailer.service.FileWriterService;
import com.markus.MassMailer.service.Settings;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MassMail implements Runnable{
    private ArrayList<String> filenames;
    private ArrayList<User> users;
    private ArrayList<ErrorMessage> errors;
    private Template bodyTemplate;
    private Template subjectTemplate;
    private MailData staticMailData;
    private MailReference mailReference;
    private MailReferenceRepository mailReferenceRepository;


    public MassMail(Template body, Template subject, ArrayList<User> users, MailData mailData, MailReference mailReference){
        errors = new ArrayList<>();
        this.bodyTemplate = body;
        this.subjectTemplate = subject;
        this.users = users;
        this.staticMailData = mailData;
        this.mailReference = mailReference;
        filenames = new ArrayList<>();
    }

    public boolean send(){
        mailReference.setStatus(MailReference.Status.SENDING);
        mailReferenceRepository.save(mailReference);
        int i = 1;
        for (String filename:filenames) {
            try{
                FileReaderService reader = new FileReaderService(filename);
                ArrayList<Mail> mails =  reader.read();
                for (Mail mail:mails) {
                    System.out.println("\n\nMail " + i);
                    i++;
                    mail.send();
                }
            } catch (IOException | ClassNotFoundException | MessagingException e) {
                errors.add(new ErrorMessage(e.getMessage()));
            }
        }
        mailReference.setStatus(MailReference.Status.SENT);
        mailReferenceRepository.save(mailReference);
        return true;
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public void parse(){
        List<List<User>> user_batches = Lists.partition(users, Settings.batchsize);
        for (List<User> users:user_batches) {
            try {
                FileWriterService writer = new FileWriterService();

                filenames.add(writer.getFilename());
                for (int i = 0; i < users.size(); i++) {
                    try {
                        Mail mail = new Mail(staticMailData, users.get(i));
                        mail.parse(bodyTemplate, subjectTemplate);
                        writer.addMail(mail);
                    } catch (MailCantGetParsedException e) {
                        errors.add(new ErrorMessage(e.getMessage(), users.get(i), i));
                    }
                }
                writer.write();
                writer.close();
            }catch (IOException e){
                errors.add(new ErrorMessage(e.getMessage()));
            }
        }
        setToReference(MailReference.Status.PARSED);
    }

    private void setToReference(MailReference.Status status){
        if(hasErrors()){
            switch (status)
            {
                case PARSED:
                    mailReference.setErrors(this.errors);
                    mailReference.setStatus(MailReference.Status.ERROR_PARSING);
                    mailReferenceRepository.save(mailReference);
                    break;
                case SENT:
                    mailReference.setErrors(this.errors);
                    mailReference.setStatus(MailReference.Status.ERROR_SENDING);
                    mailReferenceRepository.save(mailReference);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + status);
            }
        }else {
            mailReference.setStatus(status);
            mailReferenceRepository.save(mailReference);
        }
    }

    public ArrayList<String> getFilenames(){
        return filenames;
    }

    @Override
    public void run() {
        this.parse();
        if(this.hasErrors()){
            new FileCleanUpService(this.getFilenames()).clean();
        }else{
            this.send();
        }
    }

    public void setReferenceRepostory(MailReferenceRepository mailReferenceRepository) {
        this.mailReferenceRepository = mailReferenceRepository;
    }
}
