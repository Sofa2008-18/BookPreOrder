
package com.bookstore.bookpreorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookPreorderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookPreorderApplication.class, args);
	}
}
