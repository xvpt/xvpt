package org.xvpt.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class XvptApplication {

	public static void main(String[] args) {
		SpringApplication.run(XvptApplication.class, args);
	}

}
