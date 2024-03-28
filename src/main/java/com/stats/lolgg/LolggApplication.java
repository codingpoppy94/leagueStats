package com.stats.lolgg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.stats.lolgg.mapper")
public class LolggApplication {

	public static void main(String[] args) {
		SpringApplication.run(LolggApplication.class, args);
	}

}
