package com.example.exshop_main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.exshop_main.dao")
public class ExshopMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExshopMainApplication.class, args);
	}
}
