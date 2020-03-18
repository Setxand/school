package org.school.app.tennisapp.service;

import org.school.app.model.TestBox;
import org.school.app.service.TestBoxService;
import org.school.app.tennisapp.client.TelegramClient;
import org.school.app.tennisapp.config.DictionaryKeysConfig;
import org.school.app.tennisapp.model.User;
import org.school.app.tennisapp.model.User.UserStatus;
import org.springframework.stereotype.Service;
import telegram.Message;

import java.util.List;
import java.util.stream.Collectors;

import static org.school.app.tennisapp.config.DictionaryKeysConfig.*;
import static org.school.app.tennisapp.model.User.UserStatus.CHOOSE_TEXT_BOX;
import static org.school.app.tennisapp.util.DictionaryUtil.getDictionaryValue;

@Service
public class TestService {

	private final TelegramClient telegramClient;
	private final TestBoxService testBoxService;

	public TestService(TelegramClient telegramClient, TestBoxService testBoxService) {
		this.telegramClient = telegramClient;
		this.testBoxService = testBoxService;
	}

	public void startTest(Message message, User user) {
		telegramClient.simpleMessage(getDictionaryValue(DictionaryKeysConfig.CHOOSE_TEXT_BOX), message);
		user.setStatus(CHOOSE_TEXT_BOX);
	}

	public void chooseTestBoxByStatus(Message message, User user) {
		List<TestBox> testBoxesByName = testBoxService.getTestBoxesByName(message.getText());
		List<String> collect = testBoxesByName.stream().map(TestBox::getName).collect(Collectors.toList());
		telegramClient.simpleMessage(collect.toString(), message);
	}
}
