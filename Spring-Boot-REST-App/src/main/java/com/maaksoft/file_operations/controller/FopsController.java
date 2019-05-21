package com.maaksoft.file_operations.controller;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.multipart.MultipartFile;

import com.maaksoft.file_operations.payload.FileResponse;
import com.maaksoft.file_operations.payload.Test;

import com.maaksoft.file_operations.service.Utils;

import com.maaksoft.file_operations.service.FopsService;

@RestController
public class FopsController {

    private static final Logger logger = LoggerFactory.getLogger(FopsController.class);

    @Autowired
    private FopsService fileStorageService;

    @PostMapping("/uploadFile")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        String fileName = this.fileStorageService.storeFile(file);
        String fileDownloadUri = Utils.getFileDownloadUri(fileName);

        return new FileResponse(fileName, fileDownloadUri, file.getSize());

    }

    @PostMapping("/uploadMultipleFiles")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());

    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        // Load file as Resource
        Resource resource = this.fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getSession().getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    @GetMapping("/getInfo")
    public Test[] getInfo(HttpServletRequest request) {
        return new Test[] { new Test("com.maaksoft.TestClass1", 5372), new Test("com.maaksoft.TestClass2", 34781) };
    }

    @GetMapping("/getFiles")
    public List<FileResponse> getFiles(HttpServletRequest request) throws IOException {
        return this.fileStorageService.getFilesInfo();
    }

}