package com.cloud.sbserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SbServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbServerApplication.class, args);
	}

	@GetMapping(value = "/")
	public String doGetHelloWorld() {
		return "Hello World I'm sb-server";
	}

	@GetMapping(value = "/test")
	public String test() {
		return "sb-server update test";
	}
}
