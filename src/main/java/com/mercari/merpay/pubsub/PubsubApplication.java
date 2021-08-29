package com.mercari.merpay.pubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class PubsubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PubsubApplication.class, args);
	}

}
