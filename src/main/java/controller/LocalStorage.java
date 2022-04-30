package controller;

import model.LoginCredential;
import model.PGConfigDelta;
import model.TestPlan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalStorage {
    private String CREDENTIALS_FILE_LOCATION = System.getProperty("user.home") + File.separator + "pg_agent_gui" + File.separator + "credentials.ser";
    private String CONFIG_DIR_LOCATION = System.getProperty("user.home") + File.separator + "pg_agent_gui" + File.separator + "config_files" + File.separator;
    private String TEST_PLAN_LOCATION = System.getProperty("user.home") + File.separator + "pg_agent_gui" + File.separator + "test_plans" + File.separator;

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
                file.getParentFile().mkdir();
                file.createNewFile();
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

    public List<PGConfigDelta> getConfigDeltaFromFile(File file) {
        List<PGConfigDelta> resultList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_DIR_LOCATION + file.getName());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<PGConfigDelta> persistedList = (List<PGConfigDelta>) objectInputStream.readObject();
            resultList.addAll(persistedList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public boolean saveConfigFile(String fileName, List<PGConfigDelta> deltaList) {
        File directory = new File(CONFIG_DIR_LOCATION);
        File destinationFile = new File(CONFIG_DIR_LOCATION + fileName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(deltaList);
            objectOutputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean configFileExists(String fileName) {
        File destinationFile = new File(CONFIG_DIR_LOCATION + fileName);
        return destinationFile.exists();
    }

    public boolean planFileExists(String fileName) {
        File destinationFile = new File(TEST_PLAN_LOCATION + fileName);
        return destinationFile.exists();
    }



    public boolean deleteConfigFile(String fileName) {
        File destinationFile = new File(CONFIG_DIR_LOCATION + fileName);
        return destinationFile.delete();
    }

    public File[] getTestPlans() {
        File testPlanDirectory = new File(TEST_PLAN_LOCATION);
        if (!testPlanDirectory.exists()) {
            testPlanDirectory.mkdirs();
        }
        return testPlanDirectory.listFiles();
    }

    public boolean saveTestPlan(String fileName, TestPlan testPlan) {
        File directory = new File(TEST_PLAN_LOCATION);
        File destinationFile = new File(TEST_PLAN_LOCATION + fileName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(testPlan);
            objectOutputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public TestPlan getTestPlanFromFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(TEST_PLAN_LOCATION + file.getName());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            TestPlan result = (TestPlan) objectInputStream.readObject();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
