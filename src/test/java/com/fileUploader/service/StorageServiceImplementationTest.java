package com.fileUploader.service;

import com.fileUploader.config.StorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class StorageServiceImplementationTest {

    private StorageProperties properties = new StorageProperties();
    private StorageService storageService;

    @BeforeEach
    void setUp() {
        properties.setLocation("target/files/" + Math.abs(new Random().nextInt()));
        storageService = new StorageServiceImplementation(properties);
        storageService.init();
    }

    @Test
    @DisplayName("Load non existent file")
    void loadNonExistentFileTest() {
        assertThat(storageService.load("foo.txt")).doesNotExist();
    }

    @Test
    @DisplayName("Save file and then load file")
    void saveAndLoadFileTest() {
        MultipartFile file = new MockMultipartFile("foo", "foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
        storageService.store(file);
        assertThat(storageService.load(file.getOriginalFilename())).exists();
    }
}