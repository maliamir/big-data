package com.maaksoft.hadoop.factory;

import com.maaksoft.file_operations.property.FopsProperties;

import com.maaksoft.hadoop.model.HBaseStorage;
import com.maaksoft.hadoop.model.HStorage;

public class HBaseFactory implements HFactory {

    public HStorage create(FopsProperties fopsProperties) {
        return new HBaseStorage(fopsProperties);
    }

}
