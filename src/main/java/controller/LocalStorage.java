package controller;

import model.LoginCredential;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalStorage {
    private String CREDENTIALS_FILE_LOCATION = System.getProperty("user.home") + File.separator + "pg_agent_gui" + File.separator + "credentials.ser";
    private LocalStorage() {}
    private static LocalStorage instance;
    public static LocalStorage getInstance() {
        if (instance == null) {
            instance = new LocalStorage();
        }
        return instance;
    }

    public List<LoginCredential> getLoginCredentials() {
        List<LoginCredential> resultList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(CREDENTIALS_FILE_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<LoginCredential> persistedList = (List<LoginCredential>) objectInputStream.readObject();
            resultList.addAll(persistedList);
        } catch ( Exception e){
            e.printStackTrace();
        }
        return resultList;
    }

    public void saveLoginCredentials(List<LoginCredential> credentialList) {
        try {
            File file = new File(CREDENTIALS_FILE_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(credentialList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
