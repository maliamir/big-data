package com.maaksoft.hadoop;

import com.maaksoft.file_operations.property.FopsProperties;

import com.maaksoft.hadoop.factory.HFactory;

public class HFactoryProvider {

    public static HFactory getFactory(Class hClass)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return (HFactory)Class.forName(hClass.getName()).newInstance();
    }

}