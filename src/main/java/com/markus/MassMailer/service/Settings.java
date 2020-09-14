package com.markus.MassMailer.service;

import org.apache.commons.lang3.RandomStringUtils;

public class Settings {
    private static final String filepath="C:\\Users\\Markus\\Documents\\javaKurs\\mailObjects\\";
    public static final int batchsize = 2;

    public static String filepath(String filename){
        return filepath + filename;
    }

    public static String random_filename() {
        int length = 20;
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}
