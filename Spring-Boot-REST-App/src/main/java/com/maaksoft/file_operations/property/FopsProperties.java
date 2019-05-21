package com.maaksoft.file_operations.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FopsProperties {

    private String uploadDir;
    private String hdfsServerUrl;
    private String allowedUrls;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getAllowedUrls() {
        return allowedUrls;
    }

    public void setAllowedUrls(String allowedUrls) {
        this.allowedUrls = allowedUrls;
    }

    public String getHdfsServerUrl() {
        return hdfsServerUrl;
    }

    public void setHdfsServerUrl(String hdfsServerUrl) {
        this.hdfsServerUrl = hdfsServerUrl;
    }

}