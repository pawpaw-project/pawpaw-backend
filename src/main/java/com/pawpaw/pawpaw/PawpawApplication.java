package com.pawpaw.pawpaw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PawpawApplication {

	public static void main(String[] args) {
		SpringApplication.run(PawpawApplication.class, args);
	}

}
