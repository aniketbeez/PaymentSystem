package com.github.aniketbeez.paymentriskengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentriskengineApplication {
	private static final Logger logger = LoggerFactory.getLogger(PaymentriskengineApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PaymentriskengineApplication.class, args);
		logger.info("Running...");
	}

}
