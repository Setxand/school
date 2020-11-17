package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.model.Question;
import org.school.app.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ServerStart {

	@Autowired TelegramClient telegramClient;
	@Autowired
	QuestionRepository questionRepository;

	@PostConstruct
	public void setUp() {
//		//todo delete after update
//		int page = 0;
//		PageRequest pageRequest = new PageRequest(page, 100);
//
//		List<TestBox> testBoxes = testBoxService.getTestBoxes(pageRequest).getContent();
//		Map<String, Integer> testBoxesQuestionsSize = testBoxes.stream()
//				.collect(Collectors.toMap(TestBox::getId, tb -> tb.getQuestions().size()));
//
//		Page<TestProcess> all = testProcessRepository.findAll(pageRequest);
//		while (!all.isEmpty()) {
//			all.forEach(a -> {
//				if (a.getJournalMark() == 0) {
//					a.setJournalMark(a.getMark() * 12 / testBoxesQuestionsSize.get(a.getCurrentTestId()));
//				}
//			});
//			testProcessRepository.saveAll(all);
//		}

		List<Question> all = questionRepository.findAll();
		all.forEach(a -> {
			a.setCorrectAnswer(a.getCorrectAnswer().trim());
		});
		questionRepository.saveAll(all);

		telegramClient.setWebHooks();
	}

}
