package com.learningcrew.linkup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages= "com.learningcrew.linkup.common")
public class LinkupApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkupApplication.class, args);
	}

}
