package com.invest.honduras.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "storage", ignoreUnknownFields = false)
public class PropertiesStorage {

	private String volumenPath;

	

}