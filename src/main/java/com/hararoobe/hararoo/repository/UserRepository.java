package com.hararoobe.hararoo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hararoobe.hararoo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailId(String username);

	boolean existsByEmailId(String emailId);
	
	boolean existsByEmailIdAndPassword(String emailId,String password);


}
