package com.ssafy.mbotc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mbotc.dao.NoticeRepository;
import com.ssafy.mbotc.dao.UserRepository;
import com.ssafy.mbotc.entity.Notice;
import com.ssafy.mbotc.entity.User;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	NoticeRepository noticeRepository;
	
	@Override
	public Optional<User> findByUserEmailAndUrl(String userEmail, String url) {
		Optional<User> user = userRepository.findByUserEmailAndUrl(userEmail, url);
		return user;
	}
	
	@Override
	public User updateUserToken(User user, String newToken) {
		user.setToken(newToken);
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		List<Notice> notices = noticeRepository.findAllByUserId(user.getId());
		for(Notice n : notices) {
			noticeRepository.delete(n);
		}
		userRepository.delete(user);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByToken(String token) {
		Optional<User> user = userRepository.findByToken(token);
		return user;
	}

	@Override
	public Optional<User> findByUserId(String userId) {
		Optional<User> user = userRepository.findByUserId(userId);
		return user;
	}

}
