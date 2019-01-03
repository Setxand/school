package org.school.app.repository;

import org.school.app.model.TestBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestBoxRepository extends JpaRepository<TestBox, String> {
}
