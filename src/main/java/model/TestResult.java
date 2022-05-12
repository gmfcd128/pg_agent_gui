package model;

import java.util.List;

public class TestResult {
    private String name;
    private List<PGConfigDelta> configuration;
    private double totalTime;
    private boolean success;
    public TestResult(String name, List<PGConfigDelta> configuration, double totalTime, boolean success) {
        this.name = name;
        this.configuration = configuration;
        this.totalTime = totalTime;
        this.success = success;
    }

    public List<PGConfigDelta> getConfiguration() {
        return configuration;
    }

    public String getName() {
        return name;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public boolean isSuccess() {
        return success;
    }
}
