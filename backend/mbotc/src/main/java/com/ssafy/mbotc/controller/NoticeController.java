package com.ssafy.mbotc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ssafy.mbotc.entity.Channel;
import com.ssafy.mbotc.entity.Notice;
import com.ssafy.mbotc.entity.User;
import com.ssafy.mbotc.entity.request.ReqNoticePost;
import com.ssafy.mbotc.entity.request.ReqPluginNotice;
import com.ssafy.mbotc.entity.response.ResNoticeList;
import com.ssafy.mbotc.entity.response.ResRedisChannel;
import com.ssafy.mbotc.entity.response.ResRedisTeam;
import com.ssafy.mbotc.entity.response.ResRedisUser;
import com.ssafy.mbotc.service.ChannelService;
import com.ssafy.mbotc.service.NoticeService;
import com.ssafy.mbotc.service.RedisService;
import com.ssafy.mbotc.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/notification")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	// 플러그인에서 전송되는 공지를 db에 저장
	@PostMapping
	@ApiOperation(
			value = "Post notice from plugin to DB", 
			notes = "- http://localhost:8080/api/v1/notification\n- header : -")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 500, message = "FAIL")
	})
	public ResponseEntity<String> postFromPlugin(@RequestBody ReqPluginNotice notice){
		if(noticeService.findByNoticeId(notice.getPost_id()) != null) // 이미 있는 공지
			return ResponseEntity.status(500).body("Fail");
		try {
			String userId = notice.getUser_id();
			Optional<User> user = userService.findByUserId(userId);
			Optional<Channel> channel = channelService.findByToken(notice.getChannel_id());
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK);
			
			Notice saveNotice = new Notice();
			
			// file이 있으면
			if(notice.getFile_ids() != null) {
				StringBuilder sb = new StringBuilder();
				for (String s : notice.getFile_ids()) {
					sb.append(s).append(",");
				}
				String files = sb.toString();
				files = files.substring(0, files.length()-1);
				saveNotice.setFiles(files);
			}
			
			saveNotice.setChannel(channel.get());
			saveNotice.setContent(notice.getMessage());
			saveNotice.setEndTime(df.parse(notice.getEnd_time()));
			saveNotice.setStartTime(df.parse(notice.getStart_time()));
			saveNotice.setToken(notice.getPost_id());
			saveNotice.setUser(user.get());
			saveNotice.setTime(new Date());
			
			Notice result = noticeService.save(saveNotice);
			
			try {
				template.convertAndSend("/sub/notification/" + user.get().getToken(), result);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("소켓 통신 에러!!");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Fail");
		}

		return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
	}
	
	// 한달 전체 공지 갖고오기 ; 해당하는 연도별 + 월별 알림을 모두 가져온다.
	@GetMapping(value = "/month")
	@ApiOperation(
			value = "Get All Notices by Year and Month", 
			notes = "- http://localhost:8080/api/v1/notification/month?year=2021&month=05\n- header : { \"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
	public ResponseEntity<ResNoticeList> getNoticeByMonth(@RequestHeader HashMap<String,String> header, @RequestParam String year, @RequestParam String month){
		String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
		}
		String keyid = target.get().getUserId();
		
		//토큰을 기준으로 redis에 저장되어있는 구독 팀, 채널 가져옴
		ResRedisUser redisUserinfo = redisService.getUserSettings(keyid);
		
		//구독 팀 갯수
		int N = redisUserinfo.getTeams().size();
		List<ResRedisTeam> teams = redisUserinfo.getTeams();
		
		//channel id 담기
		List<Long> subscribeChannelidlist = new ArrayList<>();
		
		//channel list
		List<ResRedisChannel> channelsList = new ArrayList<ResRedisChannel>();
		StringBuilder channelTokenSB = new StringBuilder();
		for(int i = 0; i< N; i++) {
			channelsList= teams.get(i).getSubscribe();
			int K = channelsList.size();
			for(int j = 0; j < K; j++) {
				if(channelsList.get(j).isShow() == true) {
					String channelIdTemp = channelsList.get(j).getChannelId();
					channelTokenSB.append(channelIdTemp).append(",");
					long channelId = channelService.findByToken(channelIdTemp).get().getId();
					subscribeChannelidlist.add(channelId);
				}
			}
		}
			
		ResNoticeList result = new ResNoticeList();
		String subscribes = channelTokenSB.toString();
		
		if(subscribes.length() == 0) { // 구독 채널 없음
			result.setNotifications(new ArrayList<Notice>());
			result.setSubscribe("");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
		
		subscribes = subscribes.substring(0, subscribes.length()-1);
		result.setSubscribe(subscribes);
		
		//구독 채널의 한달치 공지
		List<Notice> temp = noticeService.getNoticeByYearAndMonth(year, month, subscribeChannelidlist);

		result.setNotifications(temp);

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	//일별 알람 가져오기
	@GetMapping(value = "/day")
	@ApiOperation(
			value = "Get All Notices by Year and Month And Day", 
			notes = "- http://localhost:8080/api/v1/notification/day?year=2021&month=10&day=08\n- header : { \"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
	public ResponseEntity<ResNoticeList> getNoticeByDay(@RequestHeader HashMap<String,String> header, @RequestParam String year, @RequestParam String month, @RequestParam String day){
		String authToken = header.get("auth");
		
		Optional<User> target = userService.findByToken(authToken);
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
		}
		String keyid = target.get().getUserId();
		
		//토큰을 기준으로 redis에 저장되어있는 구독 팀, 채널 가져옴
		ResRedisUser redisUserinfo = redisService.getUserSettings(keyid);
		
		//구독 팀 갯수
		int N = redisUserinfo.getTeams().size();
		List<ResRedisTeam> teams = redisUserinfo.getTeams();
		
		//channel id 담기
		List<Long> subscribeChannelidlist = new ArrayList<>();
		
		//channel list
		StringBuilder channelTokenSB = new StringBuilder();
		List<ResRedisChannel> channelsList = new ArrayList<ResRedisChannel>();
		for(int i = 0; i< N; i++) {
			channelsList= teams.get(i).getSubscribe();
			int K = channelsList.size();
			for(int j = 0; j < K; j++) {
				if(channelsList.get(j).isShow() == true) {
					String channelIdTemp = channelsList.get(j).getChannelId();
					channelTokenSB.append(channelIdTemp).append(",");
					long channelId = channelService.findByToken(channelIdTemp).get().getId();
					subscribeChannelidlist.add(channelId);
				}
			}
		}
			
		ResNoticeList result = new ResNoticeList();
		String subscribes = channelTokenSB.toString();
		
		if(subscribes.length() == 0) { // 구독 채널 없음
			result.setNotifications(new ArrayList<Notice>());
			result.setSubscribe("");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
		
		subscribes = subscribes.substring(0, subscribes.length()-1);
		result.setSubscribe(subscribes);
		
		//구독 채널의 한달치 공지
		List<Notice> temp = noticeService.getNoticeByYearAndMonthAndDay(year, month, day, subscribeChannelidlist);

		result.setNotifications(temp);

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
    //일별 알람 가져오기
    @GetMapping(value = "/today")
    @ApiOperation(
            value = "Get All today's Notices for plugin",
            notes = "- http://localhost:8080/api/v1/notification/today\n- header : { \"userId\" : \"user's id\" }")
    @ApiResponses({
        @ApiResponse(code = 200, message = "SUCCESS"),
        @ApiResponse(code = 404, message = "USER NOT FOUND")
    })
    public ResponseEntity<List<ReqNoticePost>> getTodayNoticeForPlugin(@RequestHeader HashMap<String,String> header){
        Optional<User> target = userService.findByUserId(header.get("userid"));
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
		}
		String keyid = target.get().getUserId();
			
		//토큰을 기준으로 redis에 저장되어있는 구독 팀, 채널 가져옴
		ResRedisUser redisUserinfo = redisService.getUserSettings(keyid);
			
		//구독 팀 갯수
		int N = redisUserinfo.getTeams().size();
		List<ResRedisTeam> teams = redisUserinfo.getTeams();
			
		//channel id 담기
		List<Long> subscribeChannelidlist = new ArrayList<>();
			
		//channel list
		List<ResRedisChannel> channelsList = new ArrayList<ResRedisChannel>();
		for(int i = 0; i< N; i++) {
			channelsList= teams.get(i).getSubscribe();
			int K = channelsList.size();
			for(int j = 0; j < K; j++) {
				if(channelsList.get(j).isShow() == true) {
					long channelId = channelService.findByToken(channelsList.get(j).getChannelId()).get().getId();
					subscribeChannelidlist.add(channelId);
				}
			}
		}
			
		//구독 채널의 오늘 공지
		List<ReqNoticePost> result = noticeService.getTodayNoticeList(subscribeChannelidlist);

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	// 공지 세부정보 갖고오기
	@GetMapping(value = "/post/{postId}")
	@ApiOperation(
			value = "Get a Notice by notice Id", 
			notes = "- http://localhost:8080/api/v1/notification/post/p1\n- header : { \"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 401, message = "UNAUTHRIZED")
	})
	public ResponseEntity<Notice> getNoticeByMonth1(@RequestHeader HashMap<String,String> header, @PathVariable String postId){
		String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHRIZED");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(noticeService.findByNoticeId(postId));
	}
	
	@DeleteMapping(value = "/delete/{postId}")
	@ApiOperation(
			value = "Delete a Post by post Id",
			notes = "- http://localhost:8080/api/v1/notification/delete/p1\n- header : { \"auth\" : \"user's token\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "POST NOT FOUND, DELETE FAIL"),
		@ApiResponse(code = 405, message = "YOUR NOT OWNER OF THIS POST")
	})
	public ResponseEntity<String> deletePost(@RequestHeader HashMap<String, String> header, @PathVariable String postId){
		String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
		String mattermostUrl = target.get().getUrl();
		
		Notice post = noticeService.findByNoticeId(postId);
		
		if(post.getUser().getId() != target.get().getId()) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "YOUR NOT OWNER OF THIS POST");
		}
		
		String DELETE_URL= mattermostUrl + "/api/v4/posts/"+ postId;

		
		try {
			URL url = new URL(DELETE_URL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Authorization", "bearer "+target.get().getToken());
			conn.setRequestProperty("Content-Type", "application/json");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			if(conn.getResponseCode() != 200) {
				System.out.println("Failed: HTTP error code : " + conn.getResponseCode());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POST NOT FOUND, DELETE FAIL");
			}
			br.close();
			conn.disconnect();
			noticeService.deleteByToken(postId);
		}
		catch(IOException e) {
			System.out.println("Delete Fail : " + e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
	}
	
	
	// content 내용 검색
	@GetMapping(value = "/search")
	@ApiOperation(
			value = "Get All Notices by Searching word", 
			notes = "- http://localhost:8080/api/v1/notification/search?word=여러분\n- header : { \"auth\" : \"user's token\", \"Content-Type\" : \"application/json;charset=utf-8\" }")
	@ApiResponses({
		@ApiResponse(code = 200, message = "SUCCESS"),
		@ApiResponse(code = 404, message = "USER NOT FOUND")
	})
	public ResponseEntity<ResNoticeList> getNoticeSearchByWord(@RequestHeader HashMap<String,String> header, @RequestParam String word){
		String authToken = header.get("auth");
		Optional<User> target = userService.findByToken(authToken);
		if(!target.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
		}
		String keyid = target.get().getUserId();
		
		//토큰을 기준으로 redis에 저장되어있는 구독 팀, 채널 가져옴
		ResRedisUser redisUserinfo = redisService.getUserSettings(keyid);
		
		//구독 팀 갯수
		int N = redisUserinfo.getTeams().size();
		List<ResRedisTeam> teams = redisUserinfo.getTeams();
		
		//channel id 담기
		List<Long> subscribeChannelidlist = new ArrayList<>();
		
		//channel list
		StringBuilder channelTokenSB = new StringBuilder();
		List<ResRedisChannel> channelsList = new ArrayList<ResRedisChannel>();
		for(int i = 0; i< N; i++) {
			channelsList= teams.get(i).getSubscribe();
			int K = channelsList.size();
			for(int j = 0; j < K; j++) {
				if(channelsList.get(j).isShow() == true) {
					String channelIdTemp = channelsList.get(j).getChannelId();
					channelTokenSB.append(channelIdTemp).append(",");
					long channelId = channelService.findByToken(channelIdTemp).get().getId();
					subscribeChannelidlist.add(channelId);
				}
			}
		}
			
		ResNoticeList result = new ResNoticeList();
		String subscribes = channelTokenSB.toString();
		
		if(subscribes.length() == 0) { // 구독 채널 없음
			result.setNotifications(new ArrayList<Notice>());
			result.setSubscribe("");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
		
		subscribes = subscribes.substring(0, subscribes.length()-1);
		result.setSubscribe(subscribes);

		//검색단어와 구독 채널을 넘겨서 해당하는 notice를 받는다.
		List<Notice> temp = noticeService.getNoticeSearch(word, subscribeChannelidlist);

		result.setNotifications(temp);

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	
	
}
