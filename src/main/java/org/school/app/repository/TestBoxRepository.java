package org.school.app.repository;

import org.school.app.model.TestBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestBoxRepository extends JpaRepository<TestBox, String> {

	@Query(nativeQuery = true, value = "select * from s_test_box tb where tb.name like %?1%")
	Page<TestBox> findByName(String name, Pageable pageable);

	List<TestBox> findByIdIn(List<String> testIds);
}
