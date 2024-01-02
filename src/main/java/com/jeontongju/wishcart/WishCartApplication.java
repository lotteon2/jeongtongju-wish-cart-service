package com.jeontongju.wishcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WishCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(WishCartApplication.class, args);
	}

}
