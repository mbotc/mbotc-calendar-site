package com.ssafy.mbotc.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ssafy.mbotc.entity.User;
import com.ssafy.mbotc.service.RedisService;
import com.ssafy.mbotc.service.SyncService;
import com.ssafy.mbotc.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SyncService syncService;
	
	@Autowired
	private RedisService redisService;
	
	// after login, user token update
	@PatchMapping
	@ApiOperation(
			value = "Update userToken from DB", 
			notes = "- http://localhost:8080/api/v1/user\n - no header")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
	public ResponseEntity<User> updateUserToken(@RequestBody User user) {
		Optional<User> target = userService.findByUserEmailAndUrl(user.getUserEmail(), user.getUrl());
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
		}
		User userResult = userService.updateUserToken(target.get(), user.getToken());
		syncService.syncWithUser(userResult.getToken(), userResult.getUrl(), userResult.getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(userResult);
	}
	
	// check if user exists
	@GetMapping
	@ApiOperation(
			value = "Check if user exists in DB by user's userId",
			notes = "- http://localhost:8080/api/v1/user\n - header : { \"userId\" : \"user's userId\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
	public ResponseEntity<String> checkUserExists(@RequestHeader HashMap<String,String> header){
		Optional<User> target = userService.findByUserId(header.get("userid"));
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
		}

		return ResponseEntity.status(200).body("SUCCESS");
	}

	// for new user (register)
	@PostMapping
	@ApiOperation(
			value = "Insert new User to DB (register user) & if user is already exist, change token and username", 
			notes = "- http://localhost:8080/api/v1/user\n - no header")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 409, message = "USER ALREADY EXIST")
	})
	public ResponseEntity<User> saveUserInfo(@RequestBody User user, @CookieValue(name="MMAUTHTOKEN", required = false) String mmAuthToken) {
		User u = new User();
		Optional<User> userInfo = userService.findByUserEmailAndUrl(user.getUserEmail(), user.getUrl());
		if(user.getToken() != null && user.getToken().equals("cookie")) {
			if(userInfo.isPresent()) {
				userInfo.get().setToken(mmAuthToken);
				userInfo.get().setUserName(user.getUserName());
				u = userService.save(userInfo.get());
			}else {
				user.setToken(mmAuthToken);
				u = userService.save(user);
			}
			syncService.syncWithUser(u.getToken(), u.getUrl(), u.getUserId());
			return ResponseEntity.status(HttpStatus.OK).body(u);
		}
		
		if(userInfo.isPresent()) {
			userInfo.get().setToken(user.getToken());
			userInfo.get().setUserName(user.getUserName());
			u = userService.save(userInfo.get());
			syncService.syncWithUser(u.getToken(), u.getUrl(), u.getUserId());
			return ResponseEntity.status(HttpStatus.OK).body(u);
		}
		
		u = userService.save(user);		
		syncService.syncWithUser(u.getToken(), u.getUrl(), u.getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(u);
	}
	
	// user delete
	@DeleteMapping
	@ApiOperation(
			value = "Delete User from DB", 
			notes = "- http://localhost:8080/api/v1/user\n - header : {\"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
	public ResponseEntity<String> deleteUserInfo(@RequestHeader HashMap<String,String> header, @RequestBody User user){
		String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
		
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER IS NOT FOUND");
		}
		
		if(!target.get().getUserEmail().equals(user.getUserEmail())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
		}
		
		boolean success = redisService.deleteUserSettings(user.getUserId());
		if(!success)
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		
		userService.delete(target.get());
		return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
	}
	
	
}
