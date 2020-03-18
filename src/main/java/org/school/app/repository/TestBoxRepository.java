package org.school.app.repository;

import org.school.app.model.TestBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestBoxRepository extends JpaRepository<TestBox, String> {

	@Query(nativeQuery = true, value = "select name from s_test_box where name likе '%:name%';")
	List<TestBox> findByName(String name);

}
