package org.school.app.client;

import org.school.app.config.ChatbotConfig;
import org.school.app.config.DictionaryKeysConfig;
import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.model.User;
import org.school.app.model.UserGroup;
import org.school.app.utils.DictionaryUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import telegram.Markup;
import telegram.Message;
import telegram.TelegramRequest;
import telegram.button.Button;

import java.util.List;

@Component
public class TelegramClient extends telegram.client.TelegramClient {

	public TelegramClient(ChatbotConfig config) {
		super(config.getServer(), config.getWebhook(), config.getTelegramUrls());
	}

	public void sendTextBoxesAsButtons(List<TestBox> testBoxNames, String text, Message message) {
		Markup buttonListMarkup = createButtonListMarkup(false, testBoxNames.stream()
				.map(tb -> createInlineButton(tb.getName(), tb.getId())).toArray(Button[]::new));
		sendButtons(buttonListMarkup, text, message);
	}

	public void sendUsersAsButtons(List<User> users, String text, Message message) {
		Markup buttonListMarkup = createButtonListMarkup(false, users.stream()
				.map(u -> createInlineButton(name(u), u.getChatId().toString())).toArray(Button[]::new));
		sendButtons(buttonListMarkup, text, message);
	}

	private String name(User u) {
		String name = u.getName();
		if (u.getInternalNickName() != null) {
			name += " (" + u.getInternalNickName() + ")";
		}

		return name;
	}

	public void sendQuestion(Question question, Message message, Integer messageIdToEdit) {
		Markup buttonListMarkup = createButtonListMarkup(false,
				question.getAnswers().stream()
						.map(a -> createInlineButton(a, a.substring(0, 2))).toArray(Button[]::new));

		if (messageIdToEdit != null) {
			editMessageText(buttonListMarkup, message, question.getName());
		} else
			sendButtons(buttonListMarkup, question.getName(), message);
	}

	public void sendUserGroupNames(Page<UserGroup> userGroups, String text, Message message) {
		Markup buttonListMarkup = createButtonListMarkup(false, userGroups.stream()
				.map(u -> createInlineButton(u.getName(), u.getId())).toArray(Button[]::new));
		sendButtons(buttonListMarkup, text, message);
	}

	public void simpleMessage(DictionaryKeysConfig key, Message message) {
		simpleMessage(DictionaryUtil.getDictionaryValue(key, message.getFrom().getLanguageCode()), message);
	}

	public void editInlineButtons(Markup markup, Message message, String messageText) {
		TelegramRequest request = new TelegramRequest();
		request.command = "/editMessageReplyMarkup";
		request.messageId = message.getMessageId();
		request.setText(messageText);
		request.setChatId(message.getChat().getId());
		request.setMarkup(markup);
		request.setPlatform(message.getPlatform());
		sendMessage(request);
	}
}
