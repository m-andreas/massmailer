package com.markus.MassMailer.service;

import com.markus.MassMailer.model.mail.Mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FileReaderService {
    private ObjectInputStream inStream;

    public FileReaderService(String filename) throws IOException {
        inStream = new ObjectInputStream(new FileInputStream(Settings.filepath(filename)));
    }

    public ArrayList<Mail> read() throws IOException, ClassNotFoundException {
        return (ArrayList<Mail>) inStream.readObject();
    }
}
