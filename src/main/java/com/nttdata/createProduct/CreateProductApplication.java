package com.nttdata.createProduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;


//@ComponentScan("com.createProduct.*")
@SpringBootApplication
@EnableEurekaClient
public class CreateProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreateProductApplication.class, args);
	}

}
