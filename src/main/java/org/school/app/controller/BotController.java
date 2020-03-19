package org.school.app.controller;

import org.school.app.skhoolchatbot.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import telegram.Update;

@RestController
public class BotController {

	@Autowired private DirectionService directionService;

	@PostMapping("/webhook")
	public void getUpdate(@RequestBody Update update) {
		directionService.directUpdate(update);
	}

}
