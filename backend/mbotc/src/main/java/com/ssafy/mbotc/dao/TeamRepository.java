package com.ssafy.mbotc.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.mbotc.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	
	@Transactional
	Optional<Team> findByToken(String token);
	
}
