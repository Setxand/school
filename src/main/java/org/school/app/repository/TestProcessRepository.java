package org.school.app.repository;

import org.school.app.model.TestProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestProcessRepository extends JpaRepository<TestProcess, Integer> {

	List<TestProcess> findByUserChatIdAndActiveIsTrueOrderByCreationTimeDesc(Integer chatId);

}
