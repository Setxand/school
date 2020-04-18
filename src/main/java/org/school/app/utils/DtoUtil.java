package org.school.app.utils;

import org.school.app.dto.QuestionDTO;
import org.school.app.dto.TestBoxDTO;
import org.school.app.dto.UserDataDTO;
import org.school.app.dto.UserGroupDto;
import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.model.User;
import org.school.app.model.UserGroup;

import java.util.stream.Collectors;

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

	public static UserGroupDto userGroup(UserGroup userGroup) {
		UserGroupDto dto = new UserGroupDto();
		dto.name = userGroup.getName();
		dto.users = userGroup.getUsers().stream().map(DtoUtil::user).collect(Collectors.toList());
		return dto;
	}

	private static UserDataDTO user(User user) {
		UserDataDTO dto = new UserDataDTO();
		dto.name = user.getName();
		dto.id = user.getChatId();
		return dto;
	}
}
