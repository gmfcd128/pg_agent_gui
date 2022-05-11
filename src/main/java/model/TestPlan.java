package model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPlan implements Serializable {
    private String name;
    private int numberOfThreads;
    private int numberOfRuns;
    private String payloadDirectory;
    private String baseConfigName;
    private HashMap<PGConfigDelta, List<String>> values;

    public TestPlan(String name, int numberOfRuns, int numberOfThreads) {
        this.name = name;
        this.numberOfThreads = numberOfThreads;
        this.numberOfRuns = numberOfRuns;
        this.payloadDirectory = "";
        this.values = new HashMap<>();
    }

    public String getBaseConfigName() {
        return baseConfigName;
    }

    public void setBaseConfigName(String baseConfigName) {
        this.baseConfigName = baseConfigName;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }

    public String getPayloadDirectory() {
        return payloadDirectory;
    }

    public Map<String, String> getSQLCommands() throws IOException {
        HashMap<String, String> result = new HashMap<>();
        File f = new File(this.getPayloadDirectory());

        FilenameFilter textFilter = new FilenameFilter() {
            @Override
            public boolean accept(java.io.File file, String s) {
                return s.toLowerCase().endsWith(".txt");
            }
        };

        File[] files = f.listFiles(textFilter);
        System.out.println("SQL files to run:");
        for (File file : files) {
            System.out.println(file.getCanonicalPath());
            byte[] encoded = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
            String command = new String(encoded, Charset.defaultCharset());
            String testcase = file.getName();
            if (!result.containsKey(testcase)) {
                result.put(testcase, command);
            }

        }
        return result;
    }

    public void setPayloadDirectory(String payloadDirectory) {
        this.payloadDirectory = payloadDirectory;
    }

    public HashMap<PGConfigDelta, List<String>> getValues() {
        return this.values;
    }

    public void setValues(HashMap<PGConfigDelta, List<String>> settingsValues) {
        this.values = settingsValues;
    }
}
