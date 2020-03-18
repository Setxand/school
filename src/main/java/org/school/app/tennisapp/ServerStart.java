package org.school.app.tennisapp;

import org.school.app.tennisapp.client.TelegramClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ServerStart {

	@Autowired TelegramClient telegramClient;

	@PostConstruct
	public void setUp() {
		telegramClient.setWebHooks();
	}

}
