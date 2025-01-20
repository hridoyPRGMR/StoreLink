package com.storelink.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.SessionCookieConfig;

@Configuration
public class SessionConfig {
	
	@Bean
	public ServletContextInitializer servletContextInitializer() {
		return servletContext->{
			servletContext.setSessionTimeout(1*60);
			 SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
			 sessionCookieConfig.setHttpOnly(true); // Enhance security
//	         sessionCookieConfig.setSecure(true); // Use HTTPS (recommended in production)
		};
	}
	
}
