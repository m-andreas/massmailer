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

public class MassMail implements Runnable{
    private ArrayList<String> recipients;
    private ArrayList<String> cc;
    private ArrayList<String> bcc;
    private String subject;
    private String body;
    private ArrayList<String> filenames = new ArrayList<>();
    private ArrayList<User> users= new ArrayList<>();
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

    public boolean send() throws IOException, ClassNotFoundException, MessagingException {
        mailReference.setStatus("sending in progress");
        mailReferenceRepository.save(mailReference);
        int i = 1;
        for (String filename:filenames) {
            FileReaderService reader = new FileReaderService(filename);
            ArrayList<Mail> mails =  reader.read();
            for (Mail mail:mails) {
                System.out.println("\n\nMail " + i);
                i++;
                mail.send();
            }
        }
        mailReference.setStatus("sent");
        mailReferenceRepository.save(mailReference);
        return true;
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public ArrayList getErrors(){
        return errors;
    }

    public void parse(){
        List<List<User>> user_batches = Lists.partition(users, Settings.batchsize);
        for (List<User> users:user_batches) {
            try {
                FileWriterService writer = new FileWriterService();

                filenames.add(writer.getFilename());
                for (int i = 0; i < users.size(); i++) {
                    try {
                        Mail mail = new Mail(staticMailData);
                        mail.setUser(users.get(i));
                        mail.parse(bodyTemplate, subjectTemplate);
                        writer.addMail(mail);
                    } catch (MailCantGetParsedException e) {
                        errors.add(new ErrorMessage(e.getMessage(), users.get(i), i));
                    }
                }
                writer.write();
                writer.close();
            }catch (IOException e){

            }
        }
        setErrorToReference("parsed");
    }

    private void setErrorToReference(String status){
        if(hasErrors()){
            mailReference.setStatus("Error");
        }else {
            mailReference.setStatus(status);
            mailReferenceRepository.save(mailReference);
        }
    }

    public ArrayList<String> getFilenames(){
        return filenames;
    }

    public void setBodyTemplate(Template bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public void setSubjectTemplate(Template subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void run() {
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(this.hasErrors()){
            new FileCleanUpService(this.getFilenames()).clean();
            //return new ApiError(HttpStatus.BAD_REQUEST, "Error during Mail Creation", this.getErrors());
        }else{
            try {
                this.send();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public void setReferenceRepostory(MailReferenceRepository mailReferenceRepository) {
        this.mailReferenceRepository = mailReferenceRepository;
    }
//    public boolean setRecipients(){
//        recipients = (ArrayList<String>) userData.get("to");
//        cc = (ArrayList<String>) userData.get("cc");
//        bcc = (ArrayList<String>) userData.get("bcc");
//        return true;
//    }
}
