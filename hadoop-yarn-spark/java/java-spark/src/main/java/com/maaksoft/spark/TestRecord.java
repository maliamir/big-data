package com.maaksoft.spark;

public class TestRecord {

    private String logicalName;
    private Long duration = 0L;
    private Integer occurrences = 0;

    public TestRecord(String logicalName) {
        this.logicalName = logicalName;
    }

    public String getLogicalName() {
        return logicalName;
    }

    public void addDuration(Long duration) {
        this.duration += duration;
        this.occurrences++;
    }

    public Long getDuration() {
        return duration;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

}
