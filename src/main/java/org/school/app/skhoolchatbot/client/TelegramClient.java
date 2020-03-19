package org.school.app.skhoolchatbot.client;

import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.skhoolchatbot.config.UrlConfig;
import org.springframework.stereotype.Component;
import telegram.Markup;
import telegram.Message;
import telegram.button.Button;

import java.util.List;

@Component
public class TelegramClient extends telegram.client.TelegramClient {

	public TelegramClient(UrlConfig config) {
		super(config.getServer(), config.getWebhook(), config.getTelegramUrls());
	}

	public void sendTextBoxesAsButtons(List<TestBox> testBoxNames, String text, Message message) {
		Markup buttonListMarkup = createButtonListMarkup(false, testBoxNames.stream()
				.map(tb -> createInlineButton(tb.getName(), tb.getId())).toArray(Button[]::new));
		sendButtons(buttonListMarkup, text, message);
	}

	public void sendQuestion(Question question, Message message) {
		Markup buttonListMarkup = createButtonListMarkup(false,
				question.getAnswers().stream().map(this::createKeyboardButton).toArray(Button[]::new));

		sendButtons(buttonListMarkup, question.getName(), message);
	}
}
