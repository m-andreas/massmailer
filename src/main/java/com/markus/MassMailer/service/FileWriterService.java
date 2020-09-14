package com.markus.MassMailer.service;

import com.markus.MassMailer.model.mail.Mail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileWriterService {
    private FileOutputStream fileOut;
    private ObjectOutputStream objectOut;
    private ArrayList<Mail> mailBatch;
    private String filename;

    public FileWriterService() throws IOException {
        this.filename = Settings.random_filename();
        this.fileOut = new FileOutputStream(Settings.filepath(this.filename));
        this.objectOut = new ObjectOutputStream(fileOut);
        this.mailBatch = new ArrayList<>();
    }

    public void addMail(Mail mail){
        mailBatch.add(mail);
    }

    public void write() throws IOException {
        objectOut.writeObject(mailBatch);
    }

    public void close() throws IOException {
        objectOut.close();
    }

    public String getFilename(){
        return filename;
    }
}
