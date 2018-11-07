package com.fileUploader.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("storage")
public class StorageProperties {

    /*
    Folder location for files
     */
    private String location = "upload-dir";
}
