package controller;

import model.LoginCredential;
import model.PGConfigDelta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalStorage {
    private String CREDENTIALS_FILE_LOCATION = System.getProperty("user.home") + File.separator + "pg_agent_gui" + File.separator + "credentials.ser";
    private String CONFIG_DIR_LOCATION = System.getProperty("user.home") + File.separator + "pg_agent_gui" + File.separator + "config_files" + File.separator;

    private LocalStorage() {
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public void saveLoginCredentials(List<LoginCredential> credentialList) {
        try {
            File file = new File(CREDENTIALS_FILE_LOCATION);
            if (!file.exists()) {
                file.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(credentialList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File[] getLocalConfigFiles() {
        File configDirectory = new File(CONFIG_DIR_LOCATION);
        if (!configDirectory.exists()) {
            configDirectory.mkdirs();
        }
        return configDirectory.listFiles();
    }

    public List<PGConfigDelta> getConfigurationFromFile(File file) {
        return new ArrayList<>();
    }

    public void saveConfigLocally(String filename, List<PGConfigDelta> deltaList) {
        File destinationFile = new File(CONFIG_DIR_LOCATION + filename);
        if (!destinationFile.exists()) {
            destinationFile.mkdirs();
        }
        try{
            FileWriter fw = new FileWriter(destinationFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("name,setting,unit,vartype,min_val,max_val,enumvals");
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

    }




}
