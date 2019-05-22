package com.maaksoft.hadoop.factory;

import com.maaksoft.hadoop.model.HStorage;
import com.maaksoft.hadoop.model.HdfsStorage;

import com.maaksoft.file_operations.property.FopsProperties;

public class HdfsFactory extends HFactory {

    public HdfsFactory(FopsProperties fopsProperties) {
        super(fopsProperties);
    }

    public HStorage create() {
        return new HdfsStorage(super.fopsProperties);
    }

}
