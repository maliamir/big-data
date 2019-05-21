package com.maaksoft.hadoop.factory;

import com.maaksoft.file_operations.property.FopsProperties;

import com.maaksoft.hadoop.model.HStorage;
import com.maaksoft.hadoop.model.HdfsStorage;

public class HdfsFactory implements HFactory {

    public HStorage create(FopsProperties fopsProperties) {
        return new HdfsStorage(fopsProperties);
    }

}
