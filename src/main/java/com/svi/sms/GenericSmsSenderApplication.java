package com.svi.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GenericSmsSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenericSmsSenderApplication.class, args);
	}

}
