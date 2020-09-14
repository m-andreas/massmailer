package com.markus.MassMailer.service;

import java.io.File;
import java.util.ArrayList;

public class FileCleanUpService {
    ArrayList<String> filenames;
    public FileCleanUpService(ArrayList<String> filenames){
        this.filenames = filenames;
    }

    public void clean(){
        for (String filename:filenames) {
            File fileToDelete = new File(Settings.filepath(filename));
            fileToDelete.delete();
        }
    }
}
