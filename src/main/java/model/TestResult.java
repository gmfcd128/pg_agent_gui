package model;

public class TestResult {
    private String name;
    private int sampleNumber;
    private double totalTime;

    public TestResult(String name, int sampleNumber, double totalQueryTime) {
        this.name = name;
        this.sampleNumber = sampleNumber;
        this.totalTime = totalQueryTime;
    }
}
