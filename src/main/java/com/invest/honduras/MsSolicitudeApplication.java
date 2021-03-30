package com.invest.honduras;
 
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@SpringBootApplication
public class MsSolicitudeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSolicitudeApplication.class, args);
		
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);

	}

}
