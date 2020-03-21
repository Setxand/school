package org.school.app.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chatbot")
@Getter
@Setter
public class ChatbotConfig {

	private String server;
	private String webhook;
	private String telegramUrls;

}
