package com.Coaios.AISocialMedia;

import com.Coaios.AISocialMedia.service.FlickService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AiSocialMediaApplication {

	public static void main(String[] args) {

		SpringApplication.run(AiSocialMediaApplication.class, args);

//		ConfigurableApplicationContext context = SpringApplication.run(AiSocialMediaApplication.class, args);
//
//		FlickService flickService = context.getBean(FlickService.class);
//
//		// Run your task in a separate thread
//		new Thread(() -> {
//			while (true) {
//				flickService.flickAction();
//				try {
//					Thread.sleep(60000); // Wait for 1 minute
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//					break;
//				}
//			}
//		}).start();

	}

}
