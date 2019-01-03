package org.school.app.service;

import org.school.app.dto.QuestionDTO;
import org.school.app.dto.TestBoxDTO;
import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.repository.TestBoxRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class TestBoxService {

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

	public TestBox getTextBox(String testId) {
		return boxRepo.findById(testId).orElseThrow(() -> new IllegalArgumentException("Invalid test box ID"));
	}

	private Question createQuestion(QuestionDTO q) {
		Question question = new Question();
		question.setName(q.name);
		question.setCorrectAnswer(q.correctAnswer);
		question.setAnswers(q.answers);

		return question;
	}
}
