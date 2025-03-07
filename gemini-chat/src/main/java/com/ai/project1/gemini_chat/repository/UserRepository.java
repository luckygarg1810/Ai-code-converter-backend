package com.ai.project1.gemini_chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.project1.gemini_chat.database.User;

public interface UserRepository extends JpaRepository<User, Long> {
     
	 Optional<User> findByUsername(String username);
	 Optional<User> findByEmail(String email);
	 Optional<User> findByEmailAndProvider(String email, String provider);
	  boolean existsByUsername(String username);
	    boolean existsByEmail(String email);
}
