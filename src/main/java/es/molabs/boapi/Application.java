package es.molabs.boapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
	scanBasePackages = {
		"es.molabs.boapi.infrastructure.configuration"
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
