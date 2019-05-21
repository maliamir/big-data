package com.maaksoft.hadoop.factory;

import com.maaksoft.file_operations.property.FopsProperties;

import com.maaksoft.hadoop.model.HStorage;

public interface HFactory {

    HStorage create(FopsProperties fopsProperties);

}
