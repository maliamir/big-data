package com.maaksoft.file_operations.service;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.net.MalformedURLException;

import com.maaksoft.hadoop.HFactoryProvider;
import com.maaksoft.hadoop.factory.HFactory;
import com.maaksoft.hadoop.factory.HdfsFactory;
import org.apache.hadoop.fs.FileStatus;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MultipartFile;

import com.maaksoft.file_operations.property.FopsProperties;

import com.maaksoft.file_operations.exception.FopsException;
import com.maaksoft.file_operations.exception.FileNotFoundException;

import com.maaksoft.file_operations.payload.FileResponse;

@Service
public class FopsService {

    private final Path fileStorageLocation;

    private final FopsProperties fopsProperties;

    private final HFactory hFactory;

    @Autowired
    public FopsService(FopsProperties fopsProperties) {

        this.fopsProperties = fopsProperties;
        this.fileStorageLocation = Paths.get(this.fopsProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FopsException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        try {
            this.hFactory = HFactoryProvider.getFactory(HdfsFactory.class, this.fopsProperties);
        } catch (Exception ex) {
            throw new FopsException("Unable to create Hadoop Factory instance.", ex);
        }

    }

    public String storeFile(MultipartFile file) {

        //Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //try {

            //Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FopsException("Filename contains invalid path sequence " + fileName);
            }

            //Copy file to the target location (Replacing existing file with the same name). Commenting below 2 lines since storing file locally at Application Server side is not really needed.
            //Path targetLocation = this.fileStorageLocation.resolve(fileName);
            //Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            this.hFactory.create().store(file);

            return fileName;
        /*
        } catch (IOException ex) {
            throw new FopsException("Could not store file " + fileName + ". Please try again!", ex);
        }
        */

    }

    public List<FileResponse> getFilesInfo() {

        FileStatus[] fileStatuses = (FileStatus[])this.hFactory.create().getInfo();
        List<FileResponse> fileResponses = new ArrayList<FileResponse>();

        for (int index = 0; index < fileStatuses.length; index++) {

            FileStatus fileStatus = fileStatuses[index];
            String fileName = fileStatus.getPath().getName();
            fileResponses.add(new FileResponse(fileName, Utils.getFileDownloadUri(fileName), fileStatus.getLen()));

        }

        return fileResponses;

    }

    public Resource loadFileAsResource(String fileName) {

        try {

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }

        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }

    }

}