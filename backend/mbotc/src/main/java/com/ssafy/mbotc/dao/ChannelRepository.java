package com.ssafy.mbotc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.mbotc.entity.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
	
	@Transactional
	Optional<Channel> findByToken(String token);
	
	@Transactional
	List<Channel> findAllByTeamId(long teamId);
}
