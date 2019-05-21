package com.maaksoft.hadoop.model;

import java.io.IOException;
import java.io.OutputStream;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.io.IOUtils;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.springframework.web.multipart.MultipartFile;

import com.maaksoft.file_operations.property.FopsProperties;

public class HdfsStorage extends HStorage {

    private final static String HDFS_BASE_PATH = "inputs/";

    private static Configuration configuration;
    private static Path path;

    static {

        configuration = new Configuration();
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        configuration.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());

        path = new Path(HDFS_BASE_PATH);

    }

    public HdfsStorage(FopsProperties fopsProperties) {
        super(fopsProperties);
    }

    public void store(Object object) {

        MultipartFile file = (MultipartFile)object;

        try {

            FileSystem fileSystem = FileSystem.get(URI.create(super.fopsProperties.getHdfsServerUrl()), configuration);
            OutputStream outputStream = fileSystem.create(new Path((HDFS_BASE_PATH + file.getOriginalFilename())));
            IOUtils.copyBytes(file.getInputStream(), outputStream, configuration);

        } catch (IOException ioe) {
            System.out.println("IOException occurred due to - " + ioe);
        }

    }

    public Object[] getInfo() {

        try {
            FileSystem fileSystem = FileSystem.get(URI.create(super.fopsProperties.getHdfsServerUrl()), configuration);
            return fileSystem.listStatus(path);

        } catch (IOException ioe) {
            System.out.println("IOException occurred due to - " + ioe);
        }
        return null;

    }

}