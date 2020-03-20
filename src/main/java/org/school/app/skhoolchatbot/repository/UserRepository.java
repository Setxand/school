package org.school.app.skhoolchatbot.repository;


import org.school.app.skhoolchatbot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(nativeQuery = true, value = "select * from user tb where tb.name like %?1%")
	Page<User> findByName(String name, Pageable pageable);

}
