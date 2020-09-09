package com.stockmarket.datasheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DatasheetApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatasheetApplication.class, args);
	}

}
