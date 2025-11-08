package com.online_e_learning.virtualPathshala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.online_e_learning.virtualPathshala")
public class VirtualPathshalaApplication {
	public static void main(String[] args) {
		SpringApplication.run(VirtualPathshalaApplication.class, args);
	}
}
