package org.school.app.service.skhoolchatbot;

import org.school.app.client.Platform;
import org.school.app.model.User;
import org.springframework.stereotype.Service;
import telegram.Update;

@Service
public class DirectionService {

	private final UserService userService;
	private final MessageService messageService;
	private final CallBackQueryService callBackQUeryService;

	public DirectionService(UserService userService, MessageService messageService,
							CallBackQueryService callBackQUeryService) {
		this.userService = userService;
		this.messageService = messageService;
		this.callBackQUeryService = callBackQUeryService;
	}

	public void directUpdate(Update update) {
		if (update.getMessage() != null) {
			update.getMessage().setPlatform(Platform.COMMON);
			User user = userService.createUser(update.getMessage());
			messageService.messageFromBot(update.getMessage(), user);

		} else if (update.getCallBackQuery() != null) {
			update.getCallBackQuery().getMessage().setPlatform(Platform.COMMON);
			User user = userService.createUser(update.getCallBackQuery().getMessage());
			callBackQUeryService.callBackQueryToBot(update.getCallBackQuery(), user);
		}
	}



}
