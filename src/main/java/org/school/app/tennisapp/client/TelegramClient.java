package org.school.app.tennisapp.client;

import org.school.app.tennisapp.config.UrlConfig;
import org.school.app.tennisapp.model.User;
import org.springframework.stereotype.Component;
import telegram.Message;

@Component
public class TelegramClient extends telegram.client.TelegramClient {

	public TelegramClient(UrlConfig config) {
		super(config.getServer(), config.getWebhook(), config.getTelegramUrls());
	}

	public void sendTextBoxes(Message message, User user) {

	}
}
