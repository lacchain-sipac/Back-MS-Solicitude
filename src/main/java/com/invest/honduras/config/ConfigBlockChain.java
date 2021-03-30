package com.invest.honduras.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "configuration", ignoreUnknownFields = false)
public class ConfigBlockChain {

	private String privateKeyBackend;

}