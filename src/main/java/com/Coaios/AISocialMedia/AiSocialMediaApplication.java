package com.Coaios.AISocialMedia;

import com.Coaios.AISocialMedia.service.FlickService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AiSocialMediaApplication {

	public static void main(String[] args) {

		SpringApplication.run(AiSocialMediaApplication.class, args);

		//ApplicationContext context = SpringApplication.run(AiSocialMediaApplication.class, args);
/*
		// Get the bean from Spring context instead of using "new"
		FlickService flickService = new FlickService();

		// Run your task in a separate thread
		new Thread(() -> {
			while (true) {
				flickService.poster();
				try {
					Thread.sleep(60000); // Wait for 1 minute
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}).start();
*/
	}

}
