package com.example.exshop_main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.example.exshop_main.dao")
public class ExshopMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExshopMainApplication.class, args);
	}
}
