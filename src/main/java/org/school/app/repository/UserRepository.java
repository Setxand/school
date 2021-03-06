package org.school.app.repository;


import org.school.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(nativeQuery = true, value = "select * from user tb where tb.name like %?1% or " +
			"tb.internal_nick_name like %?1%")
	Page<User> findByName(String name, Pageable pageable);

}
