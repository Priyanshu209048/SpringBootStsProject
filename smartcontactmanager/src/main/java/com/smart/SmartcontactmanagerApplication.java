package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication	//It consists of @SpringBootConfiguration, @EnableAutoConfiguration and @ComponentScan
public class SmartcontactmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);
	}

}
