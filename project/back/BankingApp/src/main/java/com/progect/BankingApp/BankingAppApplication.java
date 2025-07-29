package com.progect.BankingApp;

import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.security.RsaKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;
@EnableConfigurationProperties(RsaKeyProperties.class)
@ComponentScan(basePackages = "com.progect.BankingApp")
@SpringBootApplication
public class BankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
		System.out.println(new Date());


	}

}
