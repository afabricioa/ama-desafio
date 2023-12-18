package br.com.fabricio.ama.amadesafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
public class AmaDesafioApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmaDesafioApplication.class, args);
	}

}
