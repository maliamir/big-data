package com.maaksoft.hadoop.model;

import com.maaksoft.file_operations.property.FopsProperties;

public abstract class HStorage {

    protected FopsProperties fopsProperties;

    public HStorage(FopsProperties fopsProperties) {
        this.fopsProperties = fopsProperties;
    }

    public FopsProperties getFopsProperties() {
        return fopsProperties;
    }

    public void setFopsProperties(FopsProperties fopsProperties) {
        this.fopsProperties = fopsProperties;
    }

    public abstract void store(Object object);

    public abstract Object[] getInfo();

}