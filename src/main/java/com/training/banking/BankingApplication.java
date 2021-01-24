package com.training.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.text.SimpleDateFormat;

@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(BankingApplication.class, args);
	}

}
