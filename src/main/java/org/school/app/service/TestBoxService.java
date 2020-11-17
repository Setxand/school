package org.school.app.service;

import org.apache.log4j.Logger;
import org.school.app.dto.QuestionDTO;
import org.school.app.dto.TestBoxDTO;
import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.repository.TestBoxRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class TestBoxService {

	private static final Logger logger = Logger.getLogger(TestBoxService.class);

	private final TestBoxRepository boxRepo;

	public TestBoxService(TestBoxRepository boxRepo) {
		this.boxRepo = boxRepo;
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
		TestBox testBox = boxRepo.findById(testId).orElseThrow(() -> new IllegalArgumentException("Invalid test box ID"));
		List<Question> questions = testBox.getQuestions();
		return testBox;
	}

	public void deleteTestBox(String testId) {
		boxRepo.deleteById(testId);
	}

	public List<TestBox> getTestBoxesByName(String name) {
		return boxRepo.findByName(name, new PageRequest(0, 4)).getContent();///todo fix
	}

	@Transactional
	public void updateTestBox(TestBoxDTO dto) {
		TestBox testBox = getTestBox(dto.id);
		Set<String> keys = dto.keys;

		if (keys.contains("questions")) {
			List<QuestionDTO> questions = dto.questions;

			List<Question> questionsModels = questions.stream().map(this::createQuestion).collect(toList());
			testBox.setQuestions(questionsModels);
		}

		if (keys.contains("name")) {
			testBox.setName(dto.name);
		}
	}

	public List<TestBox> getTestBoxesByIds(List<String> testIds) {
		return boxRepo.findByIdIn(testIds);
	}

	private Question createQuestion(QuestionDTO q) {
		Question question = new Question();
		question.setName(q.name);
		question.setCorrectAnswer(q.correctAnswer.trim());
		question.setAnswers(q.answers);

		return question;
	}
}
