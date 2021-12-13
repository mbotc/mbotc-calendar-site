package com.ssafy.mbotc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.mbotc.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
	
	@Transactional 
	@Query(value = "select * from notice where (:date between date_format(start_time,'%Y-%m') and date_format(end_time,'%Y-%m')) and channel_id in(:channelId)", nativeQuery = true)
	List<Notice> findAllByYearAndMonth(@Param("date") String date, @Param("channelId") List<Long> channelId);
	
	@Transactional
	Notice findByToken(String token);
	
	@Transactional
	@Query(value = "select * from notice where (:date between date_format(start_time,'%Y-%m-%d') and date_format(end_time,'%Y-%m-%d')) and channel_id in(:channelId)", nativeQuery = true)
	List<Notice> findAllByYearAndMonthAndDay(@Param("date") String date, @Param("channelId") List<Long> channelId);
	
	@Transactional
	List<Notice> findAllByContentContainingAndChannelIdIn(String word, List<Long> channelId);
	
	@Transactional
	List<Notice> findAllByUserId(long userId);
}
