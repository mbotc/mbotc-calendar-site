package com.ssafy.mbotc.service;

import java.util.Optional;

import com.ssafy.mbotc.entity.User;

public interface UserService {

	Optional<User> findByUserEmailAndUrl(String userEmail, String url);
	User updateUserToken(User user, String newToken);
	User save(User user);
	void delete(User user);
	Optional<User> findByToken(String token);
	Optional<User> findByUserId(String userId);
}
