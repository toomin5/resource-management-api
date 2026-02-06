package com.example.resource_management_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ResourceManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceManagementApiApplication.class, args);
	}

}
