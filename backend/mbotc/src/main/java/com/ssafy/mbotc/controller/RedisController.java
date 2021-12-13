package com.ssafy.mbotc.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.ssafy.mbotc.entity.User;
import com.ssafy.mbotc.entity.response.ResRedisUser;
import com.ssafy.mbotc.service.RedisService;
import com.ssafy.mbotc.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping("/redis")
public class RedisController {
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	UserService userService;
	
	@PostMapping(value = "/user")
	@ApiOperation(
			value = "Update UserSetting in Redis by user's token", 
			notes = "- http://localhost:8080/api/v1/redis/user\n - header : { \"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
    public ResponseEntity<String> setUserSetting(@RequestHeader HashMap<String,String> header, @RequestBody ResRedisUser body){
		String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
        if(!target.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
        }
        String keyid = target.get().getUserId();
        redisService.setUserSettings(keyid, body);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
    }

    @GetMapping(value = "/user")
    @ApiOperation(
			value = "Get UserSetting in Redis by user's token", 
			notes = "- http://localhost:8080/api/v1/redis/user\n - header : { \"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
    public ResponseEntity<ResRedisUser> getUserSetting(@RequestHeader HashMap<String,String> header){
    	String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
        if(!target.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
        }
        String keyid = target.get().getUserId();
        ResRedisUser response = redisService.getUserSettings(keyid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
    /*  ############################### CHECK LIST #######################################
	@PostMapping(value = "/checklist")
	public ResponseEntity<String> setUserChecklist(@RequestParam String userToken, @RequestBody ResRedisCheckList body){
		Optional<User> target = userService.findByToken(userToken);
		String keyid = target.get().getUserId();
		redisService.setUserChecklists(keyid, body);
		return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
		
	}
	
	@GetMapping(value = "/checklist")
	public ResponseEntity<ResRedisCheckList> getUserChecklist(@RequestParam String usertoken){
		Optional<User> target = userService.findByToken(usertoken);
		String keyid = target.get().getUserId();
		ResRedisCheckList response = redisService.getUserChecklist(keyid);
		if(response == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POSTS NOT FOUND");
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	*/
}
