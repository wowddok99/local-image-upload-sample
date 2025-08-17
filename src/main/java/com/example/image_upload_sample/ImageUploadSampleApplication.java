package com.example.image_upload_sample;

import com.example.image_upload_sample.config.ImageUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ImageUploadProperties.class)
public class ImageUploadSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageUploadSampleApplication.class, args);
	}

}
