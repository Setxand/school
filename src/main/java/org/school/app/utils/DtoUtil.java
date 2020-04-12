package org.school.app.utils;

import org.school.app.dto.QuestionDTO;
import org.school.app.dto.TestBoxDTO;
import org.school.app.model.Question;
import org.school.app.model.TestBox;

import static java.util.stream.Collectors.toList;

public class DtoUtil {

	public static TestBoxDTO testBox(TestBox testBox) {
		TestBoxDTO dto = new TestBoxDTO();
		dto.id = testBox.getId();
		dto.name = testBox.getName();
		dto.questions = testBox.getQuestions().stream().map(DtoUtil::question).collect(toList());
		return dto;
	}

	private static QuestionDTO question(Question question) {
		QuestionDTO dto = new QuestionDTO();
		dto.id = question.getId();
		dto.name = question.getName();
		dto.answers = question.getAnswers();
		dto.correctAnswer = question.getCorrectAnswer();
		dto.answers = question.getAnswers();
		return dto;
	}
}
