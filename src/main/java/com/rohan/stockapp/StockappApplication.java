package com.rohan.stockapp;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories
@EntityScan
@Profile("!test")
@Configuration
public class StockappApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(StockappApplication.class, args);
	}
	
	@PostConstruct
	public void hello() {
		System.out.println("HI");
	}
	
}
