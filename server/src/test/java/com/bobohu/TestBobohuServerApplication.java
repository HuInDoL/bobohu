package com.bobohu;

import org.springframework.boot.SpringApplication;

public class TestBobohuServerApplication {

	public static void main(String[] args) {
		SpringApplication.from(BobohuServerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
