package com.maaksoft.file_operations.service;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utils {

    public static String getFileDownloadUri(String fileName) {

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileName)
                .toUriString();

    }

}