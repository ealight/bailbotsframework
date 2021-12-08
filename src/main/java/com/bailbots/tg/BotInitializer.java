package com.bailbots.tg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class BotInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BotInitializer.class, args);
	}

}
