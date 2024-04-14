package com.learning.msblue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsBlueApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBlueApplication.class, args);
	}

}
