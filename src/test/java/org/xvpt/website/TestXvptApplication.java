package org.xvpt.website;

import org.springframework.boot.SpringApplication;

public class TestXvptApplication {

	public static void main(String[] args) {
		SpringApplication.from(XvptApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
