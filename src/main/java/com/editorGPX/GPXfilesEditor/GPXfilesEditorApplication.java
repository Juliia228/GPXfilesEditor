package com.editorGPX.GPXfilesEditor;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class GPXfilesEditorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GPXfilesEditorApplication.class, args);
	}
	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.parse("1MB"));
		factory.setMaxRequestSize(DataSize.parse("1MB"));
		return factory.createMultipartConfig();
	}
}
