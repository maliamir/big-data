package com.maaksoft.hadoop;

import java.lang.reflect.InvocationTargetException;

import com.maaksoft.hadoop.factory.HFactory;

import com.maaksoft.file_operations.property.FopsProperties;

public class HFactoryProvider {

    public static HFactory getFactory(Class hClass, FopsProperties fopsProperties)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return (HFactory) hClass.getDeclaredConstructor(FopsProperties.class).newInstance(fopsProperties);
    }

}