package com.maaksoft.hadoop.factory;

import com.maaksoft.file_operations.property.FopsProperties;

import com.maaksoft.hadoop.model.HStorage;

public abstract class HFactory {

    protected FopsProperties fopsProperties;

    public HFactory(FopsProperties fopsProperties) {
        this.fopsProperties = fopsProperties;
    }

    public abstract HStorage create();

}