package com.FaceTool;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SuppressWarnings("deprecation")
@SpringBootApplication
@Configuration
@EnableAutoConfiguration // Sprint Boot Auto Configuration
@ComponentScan(basePackages = "com.FaceTool")
@PropertySource({ "classpath:application.properties", "classpath:message.properties" })
public class Application extends SpringBootServletInitializer   {
	private static final Class<Application> applicationClass = Application.class;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}
}
