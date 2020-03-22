package org.school.app.repository;

import org.school.app.model.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserGroupRepository extends JpaRepository<UserGroup, String> {

	@Query(nativeQuery = true, value = "select * from user_group where name like %?1%")
	Page<UserGroup> findByNames(String name, Pageable pageable);

}
