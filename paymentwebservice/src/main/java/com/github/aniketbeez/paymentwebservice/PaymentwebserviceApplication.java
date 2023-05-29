package com.github.aniketbeez.paymentwebservice;

import com.github.aniketbeez.paymentwebservice.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class PaymentwebserviceApplication {

	private static final Logger logger = LoggerFactory.getLogger(PaymentwebserviceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(PaymentwebserviceApplication.class, args);

		logger.info("Running Payment web service...");
	}

}
