package com.maaksoft.cassandra.model;

import java.time.LocalDateTime;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

import org.springframework.data.cassandra.core.mapping.Table;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Table
public class Data {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID id;

    private java.time.LocalDateTime startTime = java.time.LocalDateTime.now();

    @PrimaryKeyColumn(name = "value", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String value;

    public Data(final UUID id, final String value) {
        this.id = id;
        this.setValue(value);
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
