package com.maaksoft.file_operations.payload;

public class Test {

    private String testName;
    private long executionTime = 100L;

    public Test(String testName, long executionTime) {
        this.testName = testName;
        this.executionTime = executionTime;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return this.testName;
    }
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

}
