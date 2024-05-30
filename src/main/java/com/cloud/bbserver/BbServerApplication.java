package com.cloud.bbserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BbServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BbServerApplication.class, args);
	}

	@GetMapping(value = "/")
	public String doGetHelloWorld() {
		return "Hello World";
	}

	@GetMapping(value = "/demo")
	public String doGetHelloWorldDemo() {
		return "Hello World (Demo)";
	}

	@GetMapping(value = "/webhook1")
	public String testWebhook1() {
		return "webhook";
	}

	@GetMapping(value = "/webhook3")
	public String testWebhook3() {
		return "webhook3";
	}

	@GetMapping(value = "/webhook4")
	public String testWebhook4() {
		return "webhook4";
	}

	@GetMapping(value = "/webhook5")
	public String testWebhook5() {
		return "webhook5";
	}
}
