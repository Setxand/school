package org.school.app.tennisapp.repository;


import org.springframework.stereotype.Repository;
import org.school.app.tennisapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByTennisId(String tennisId);

	List<User> findUsersByTennisIdIn(Set<String> keySet);

}
