package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

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
