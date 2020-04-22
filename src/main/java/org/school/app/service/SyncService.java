package org.school.app.service;

import org.school.app.controller.ControllerConstants;
import org.school.app.model.TestBox;
import org.school.app.model.User;
import org.school.app.service.skhoolchatbot.UserService;
import org.school.app.utils.DtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SyncService {
	private static final String INVALID_SYNC_PARAM = "Invalid sync parameter";

	private static final String TESTS_QUERY = "/v1/tests";
	private static final String USERS_QUERY = "/v1/users";

	private final RestTemplate restTemplate;
	private final TestBoxService testBoxService;
	private final UserService userService;

	public SyncService(TestBoxService testBoxService, UserService userService) {
		this.restTemplate = new RestTemplate();
		this.testBoxService = testBoxService;
		this.userService = userService;
	}

	public void synchronize(String query, ControllerConstants type) {
		PageRequest pageRequest = new PageRequest(0, 100);


		switch (type) {
			case TEST:
				Page<TestBox> testBoxes = testBoxService.getTestBoxes(pageRequest);

				while (!testBoxes.isEmpty()) {

					testBoxes.forEach(t -> {
						restTemplate.postForEntity(query + TESTS_QUERY, DtoUtil.testBox(t), Void.class);
					});

					testBoxes = testBoxService.getTestBoxes(pageRequest.next());
				}
				break;

			case USER:
				Page<User> users = userService.findAll(pageRequest);

				while (!users.isEmpty()) {
					restTemplate.postForEntity(query + USERS_QUERY, users.getContent(), Void.class);

					users = userService.findAll(pageRequest.next());
				}
				break;

			default:
				throw new IllegalArgumentException(INVALID_SYNC_PARAM);
		}

	}
}
