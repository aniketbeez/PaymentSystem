package com.github.aniketbeez.paymentwebservice;

import com.github.aniketbeez.paymentwebservice.config.RsaKeyProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class PaymentwebserviceApplication {
	public static void main(String[] args) {

		SpringApplication.run(PaymentwebserviceApplication.class, args);
		log.info("Running Payment web service...");
	}

}
