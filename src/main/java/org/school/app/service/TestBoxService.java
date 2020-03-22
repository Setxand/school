package org.school.app.service;

import org.apache.log4j.Logger;
import org.school.app.dto.QuestionDTO;
import org.school.app.dto.TestBoxDTO;
import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.repository.TestBoxRepository;
import org.school.app.utils.DtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TestBoxService {

	private static final Logger logger = Logger.getLogger(TestBoxService.class);

	private final TestBoxRepository boxRepo;
	private final RestTemplate restTemplate;

	public TestBoxService(TestBoxRepository boxRepo) {
		this.boxRepo = boxRepo;
		this.restTemplate = new RestTemplate();
	}

	@Transactional
	public void createTestBox(TestBoxDTO dto) {
		TestBox box = new TestBox();

		box.setName(dto.name);
		box.setQuestions(dto.questions.stream().map(this::createQuestion).collect(toList()));

		boxRepo.saveAndFlush(box);
	}

	public Page<TestBox> getTestBoxes(Pageable pageable) {
		return boxRepo.findAll(pageable);
	}

	public TestBox getTestBox(String testId) {
		return boxRepo.findById(testId).orElseThrow(() -> new IllegalArgumentException("Invalid test box ID"));
	}

	public void deleteTestBox(String testId) {
		boxRepo.deleteById(testId);
	}

	public List<TestBox> getTestBoxesByName(String name) {
		return boxRepo.findByName(name, new PageRequest(0, 4)).getContent();///todo fix
	}

	public void synchronize(String query) {
		PageRequest pageRequest = new PageRequest(0, 100);
		Page<TestBox> testBoxes = getTestBoxes(pageRequest);

		while (!testBoxes.isEmpty()) {

			testBoxes.forEach(t -> {
				restTemplate.postForEntity(query + "/v1/tests", DtoUtil.testBox(t), Void.class);
			});

			testBoxes = getTestBoxes(pageRequest.next());
		}
	}

	private Question createQuestion(QuestionDTO q) {
		Question question = new Question();
		question.setName(q.name);
		question.setCorrectAnswer(q.correctAnswer);
		question.setAnswers(q.answers);

		return question;
	}
}
