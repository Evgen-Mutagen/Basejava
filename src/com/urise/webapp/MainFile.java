package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/com/urise/webapp");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printFiles(dir);
    }

    public static void printFiles(File dir) {
        File[] files = dir.listFiles();
        String counter = " ";
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(counter + "File: " + file.getName());
                } else if (file.isDirectory()) {
                    counter += " ";
                    System.out.println(counter + "Directory: " + file.getName());
                    printFiles(file);
                }
            }
        }
    }
}
