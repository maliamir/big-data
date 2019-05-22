package com.maaksoft.hadoop.factory;

import com.maaksoft.hadoop.model.HStorage;
import com.maaksoft.hadoop.model.HBaseStorage;

import com.maaksoft.file_operations.property.FopsProperties;

public class HBaseFactory extends HFactory {

    public HBaseFactory(FopsProperties fopsProperties) {
        super(fopsProperties);
    }

    public HStorage create() {
        return new HBaseStorage(super.fopsProperties);
    }

}
