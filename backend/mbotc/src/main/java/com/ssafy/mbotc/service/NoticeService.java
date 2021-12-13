package com.ssafy.mbotc.service;

import java.util.List;

import com.ssafy.mbotc.entity.Notice;
import com.ssafy.mbotc.entity.request.ReqNoticePost;

public interface NoticeService {

	List<Notice> getNoticeByYearAndMonth(String year, String month, List<Long> channelId);
	List<Notice> getNoticeByYearAndMonthAndDay(String year, String month, String day, List<Long> channelId);
	Notice findByNoticeId(String postId);
	Notice save(Notice notice);
	List<ReqNoticePost> getTodayNoticeList(List<Long> channelId);
	void deleteByToken(String postId);
	List<Notice> getNoticeSearch(String word, List<Long> channelId);
}
