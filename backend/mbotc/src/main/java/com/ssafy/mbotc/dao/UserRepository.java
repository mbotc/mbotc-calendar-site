package com.ssafy.mbotc.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.mbotc.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Transactional
	Optional<User> findByUserEmailAndUrl(String userEmail, String url);
	@Transactional
	Optional<User> findByToken(String token);
	@Transactional
	Optional<User> findByUserId(String UserId);
	
}
