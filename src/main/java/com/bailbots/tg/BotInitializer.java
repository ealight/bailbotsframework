package com.bailbots.tg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class BotInitializer {

	public static void main(String[] args) {
		System.out.println(new Date());
		SpringApplication.run(BotInitializer.class, args);
	}

}
