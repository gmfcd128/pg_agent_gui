package model;

public class TestResult {
    private String name;
    private double totalTime;
    private boolean success;

    public TestResult(String name, double totalTime, boolean success) {
        this.name = name;
        this.totalTime = totalTime;
        this.success = success;
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
