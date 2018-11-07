package com.fileUploader;

import com.fileUploader.config.StorageProperties;
import com.fileUploader.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileUploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploaderApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args -> {
			storageService.deleteAll();
			storageService.init();
		});
	}
}
