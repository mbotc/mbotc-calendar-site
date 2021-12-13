package com.ssafy.mbotc.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.ssafy.mbotc.entity.User;
import com.ssafy.mbotc.service.SyncService;
import com.ssafy.mbotc.service.SyncService;
import com.ssafy.mbotc.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping("/sync")
public class SyncController {

	@Autowired
	SyncService syncService;
	
	@Autowired
	UserService userService;
	
	@GetMapping
	@ApiOperation(
			value = "Sync Redis' UserSetting and DB from mattermost API", 
			notes = "- http://localhost:8080/api/v1/sync\n - header: { \"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
	public ResponseEntity<String> setUserSetting(@RequestHeader HashMap<String,String> header){
		String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
		}
		syncService.syncWithUser(authToken, target.get().getUrl(), target.get().getUserId());
		return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
	}
}
