package com.ssafy.mbotc.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.ssafy.mbotc.entity.response.ResRedisChannel;
import com.ssafy.mbotc.entity.response.ResRedisTeam;
import com.ssafy.mbotc.entity.response.ResRedisUser;

@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	
	/**
	 * Redis에 저장되어 있는 userSetting을 갖고온다.
	 * @param token : DB에서 user_id에 해당되는 부분
	 * @return
	 */
	public ResRedisUser getUserSettings(String token) {
		ValueOperations<String, String> value = redisTemplate.opsForValue();
		ResRedisUser userSetting = new ResRedisUser();
		// 저장된 값이 없으면 null 반환 => parse할 때 null pointer exception 예방
		if(!redisTemplate.hasKey(token))
			return null;
		try {
			JSONParser parser = new JSONParser();
			String valS = value.get(token).toString();
			JSONObject content = (JSONObject) parser.parse(valS);
			userSetting.setTheme((String) content.get("theme"));
			JSONArray teams = (JSONArray) content.get("teams");
			List<ResRedisTeam> teamList = new ArrayList<>();
			for (int i = 0; i < teams.size(); i++) {
				ResRedisTeam team = new ResRedisTeam();
				JSONObject teamJ = (JSONObject) teams.get(i);
				team.setTeamId((String) teamJ.get("teamId"));
				team.setTeamName((String) teamJ.get("teamName"));
				team.setColor((String) teamJ.get("color"));
				
				JSONArray channels = (JSONArray) teamJ.get("subscribe");
				List<ResRedisChannel> channelList = new ArrayList<>();
				for (int j = 0; j < channels.size(); j++) {
					ResRedisChannel channel = new ResRedisChannel();
					JSONObject channelJ = (JSONObject) channels.get(j);
					channel.setChannelId((String) channelJ.get("channelId"));
					channel.setChannelName((String) channelJ.get("channelName"));
					channel.setShow(((String) channelJ.get("show")).equals("true")?true:false);
					channelList.add(channel);
				}
				team.setSubscribe(channelList);
				teamList.add(team);
			}
			userSetting.setTeams(teamList);
		}catch(Exception e) {
			System.out.println("에러!!");
			e.printStackTrace();
			return null;
		}
		return userSetting;
	}
	
	public void setUserSettings(String userToken, ResRedisUser userSetting) {
		ValueOperations<String, String> value = redisTemplate.opsForValue();
		System.out.println(userSetting.toString());
		value.set(userToken, userSetting.toString());
	}
	
	public boolean deleteUserSettings(String userId) {
		return redisTemplate.delete(userId);
	}
	
	/* ############################### CHECK LIST #######################################
	//user post checklist
	public void setUserChecklists(String keyid, ResRedisCheckList userCheckList) {
		String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String userkey = keyid.concat("_").concat(formatDate);		
		System.out.println(userkey);
		ValueOperations<String, String> value = redisTemplate.opsForValue();
		value.set(userkey, userCheckList.toString());

	}
	
	
	public ResRedisCheckList getUserChecklist(String keyid) {
		ValueOperations<String, String> value = redisTemplate.opsForValue();
		ResRedisCheckList userChecklist = new ResRedisCheckList();
		
		try {
			JSONObject content = (JSONObject) parser.parse(value.get(keyid));
			System.out.println(content.toString());
			JSONArray posts = (JSONArray) content.get("posts");
			List<Post> postList = new ArrayList<>();
			for(int i = 0; i< posts.size(); i++) {
				ResRedisCheckList postobj = new ResRedisCheckList();
				Post post = postobj.new Post();
				JSONObject postP = (JSONObject) posts.get(i);
				post.setPostId((String) postP.get("postId"));
				post.setDone((Boolean) postP.get("isDone"));
				postList.add(post);
			}
			userChecklist.setPosts(postList);
			
		}catch(Exception e) {
			System.out.println("Getting post error");
			e.printStackTrace();
			return null;
		}
		return userChecklist;
	}
	*/
}
