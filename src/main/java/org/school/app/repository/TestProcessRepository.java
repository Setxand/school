package org.school.app.repository;

import org.school.app.model.TestProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestProcessRepository extends JpaRepository<TestProcess, Integer> {

	Optional<TestProcess> findByUserChatIdAndActiveIsTrue(Integer chatId);

}
