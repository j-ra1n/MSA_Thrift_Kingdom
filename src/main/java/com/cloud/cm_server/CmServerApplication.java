package com.cloud.cm_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CmServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmServerApplication.class, args);
	}

	@GetMapping(value = "/")
	public String doGetHelloWorld() {
		return "Hello World";
	}

	@GetMapping(value = "/demo")
	public String doGetHelloWorldDemo() {
		return "Hello World (Demo)";
	}

	@GetMapping(value = "/jenkins")
	public String testJenkins() {
		return "jenkins";
	}
}
