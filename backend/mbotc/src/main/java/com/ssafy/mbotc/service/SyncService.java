package com.ssafy.mbotc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mbotc.dao.ChannelRepository;
import com.ssafy.mbotc.dao.TeamRepository;
import com.ssafy.mbotc.entity.Channel;
import com.ssafy.mbotc.entity.Team;
import com.ssafy.mbotc.entity.response.ResRedisChannel;
import com.ssafy.mbotc.entity.response.ResRedisTeam;
import com.ssafy.mbotc.entity.response.ResRedisUser;

@Service
public class SyncService {
	
	 public String[] colors = {
	            "#F9FBE7", // lime
	            "#E1F5FE", // light-blue
	            "#FFEBEE", // red
	            "#E8F5E9", // green
	            "#FFF3E0", // orange
	            "#E8EAF6", // indigo
	            "#F3E5F5", // purple
	            "#E0F2F1", // teal
	            "#B9F6CA", // green
	            "#FF9E80", // deep orange
	            "#FF8A80", // red
	            "#8C9EFF", // indigo
	            "#FFFF8D", // yellow
	            "#A7FFEB", // teal
	            "#CCFF90", // light-green
	            "#FFD180"  // orange
	    };
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	ChannelRepository channelRepository;
	
	@Autowired
	RedisService redisService;
	
	JSONParser parser = new JSONParser();
	
	public void syncWithUser(String token, String mattermostUrl, String userId) {
		List<ResRedisTeam> teams = getTeamsFromMM(token, mattermostUrl, userId);
		for (int i = 0; i < teams.size(); i++) {
			ResRedisTeam team = teams.get(i);
			List<ResRedisChannel> channels = getChannelsFromMM(token, mattermostUrl, userId, team.getTeamId());
			teams.get(i).setSubscribe(channels);
		}
		
		// mm API ????????? redis ??????
		redisAndMMAPI(teams, userId);
		
		// mm API ????????? DB ??????
		dbAndMMAPI(teams);
		
	}
	
	public void dbAndMMAPI(List<ResRedisTeam> teams) {	
		for (int i = 0; i < teams.size(); i++) {
			ResRedisTeam team = teams.get(i);
			// team token??? ???????????? db?????? ??????????????? ?????? ?????? ??? ??????????????? channel token ????????????
			Optional<Team> isTeam = teamRepository.findByToken(team.getTeamId());
			
			if(isTeam.isPresent()) { // ???????????? ??? => ?????? ??????
				if(!isTeam.get().getName().equals(team.getTeamName())) {
					Team t = isTeam.get();
					t.setName(team.getTeamName());
					teamRepository.save(t);
				}
				List<Channel> savedChannels = channelRepository.findAllByTeamId(isTeam.get().getId());
				List<ResRedisChannel> channels = team.getSubscribe();
				for (int j = 0; j < channels.size(); j++) {
					ResRedisChannel rrc = channels.get(j);
					boolean isInSavedList = false;
					for (int k = 0; k < savedChannels.size(); k++) {
						Channel sc = savedChannels.get(k);
						// channel_id?????? ?????? ?????? ???????????? ??? ????????? ??? = DB??? ????????? ????????? ?????? ???
						if(rrc.getChannelId().equals(sc.getToken())) { // ?????? DB??? ????????? ??????. ?????? ?????? ??????
							isInSavedList = true;
							if(!sc.getName().equals(rrc.getChannelName())) { // ?????? ?????? ???????????? ??????
								sc.setName(rrc.getChannelName());
								channelRepository.save(sc);
							}
							break;
						}
					}
					if(!isInSavedList) {
						channelRepository.save(new Channel(isTeam.get(), rrc.getChannelName(), rrc.getChannelId()));
					}
					
				}
			}else { // db??? ????????? ??????
				Team t = teamRepository.save(new Team(team.getTeamName(), team.getTeamId()));
				for (int j = 0; j < team.getSubscribe().size(); j++) {
					ResRedisChannel channel = team.getSubscribe().get(j);
					channelRepository.save(new Channel(t, channel.getChannelName(), channel.getChannelId()));
				}
			}
		}
	}
	
	public void redisAndMMAPI(List<ResRedisTeam> teams, String userId) {
		ResRedisUser userSetting = redisService.getUserSettings(userId);
		if(userSetting == null) { // ????????? ???????????? redis??? ???????????? ??????
			redisService.setUserSettings(userId, new ResRedisUser(teams,"light"));
		}else { // ?????? ?????????
			// Redis??? ????????? ????????? ?????? ?????? Map ????????? ????????????.
			Map<String,Map<String,Boolean>> teamSetting = new HashMap<>();
			Map<String,String> teamSettingColor = new HashMap<>();
			for (int i = 0; i < userSetting.getTeams().size(); i++) {
				ResRedisTeam team = userSetting.getTeams().get(i);
				Map<String,Boolean> channelSetting = new HashMap<>();
				for (int j = 0; j < team.getSubscribe().size(); j++) {
					ResRedisChannel channel = team.getSubscribe().get(j);
					channelSetting.put(channel.getChannelId(), channel.isShow());
				}
				teamSetting.put(team.getTeamId(), channelSetting);
				teamSettingColor.put(team.getTeamId(), team.getColor());
			}
			
			// ?????? ????????? ?????? ?????? ????????? ?????? ???????????? teams?????? (???,??????) ????????? ????????? ??????????????????.
			ResRedisUser newSetting = new ResRedisUser();
			newSetting.setTheme(userSetting.getTheme());
			List<ResRedisTeam> changeTeam = new ArrayList<>();
			for (int i = 0; i < teams.size(); i++) {
				ResRedisTeam team = teams.get(i);
				Map<String,Boolean> channelSetting = teamSetting.get(team.getTeamId());
				List<ResRedisChannel> changeChannel = new ArrayList<>(); 
				for (int j = 0; j < team.getSubscribe().size(); j++) {
					ResRedisChannel channel = team.getSubscribe().get(j);
					// ????????? ?????? ??????
					if(channelSetting!= null && channelSetting.containsKey(channel.getChannelId())) {
						changeChannel.add(new ResRedisChannel(channel.getChannelId(),channel.getChannelName(),channelSetting.get(channel.getChannelId())));
					}else { // ????????? ?????? ????????? ?????? ??????
						changeChannel.add(new ResRedisChannel(channel.getChannelId(),channel.getChannelName(), true));
					}
				}
				// ????????? ?????? ??? : ?????? ??????
				if(teamSetting.containsKey(team.getTeamId())) {
					changeTeam.add(new ResRedisTeam(team.getTeamId(),team.getTeamName(),teamSettingColor.get(team.getTeamId()),changeChannel));
				}else { // ????????? ?????? ??????
					changeTeam.add(new ResRedisTeam(team.getTeamId(),team.getTeamName(),colors[i%colors.length],changeChannel));
				}
			}// team for
			newSetting.setTeams(changeTeam);
			redisService.setUserSettings(userId, newSetting);
		}
	}
	
	public List<ResRedisTeam> getTeamsFromMM(String token, String mattermostUrl, String userId) {
		String GET_TEAM_LIST = mattermostUrl + "/api/v4/users/"+userId+"/teams";
		StringBuilder response = new StringBuilder();
		List<ResRedisTeam> teams = new ArrayList<>();
		
		try {
            URL url = new URL(GET_TEAM_LIST);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "bearer "+token);
            conn.setRequestProperty("Content-Type", "application/json");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            if (conn.getResponseCode() != 200) {
                System.out.println("Failed: HTTP error code : " + conn.getResponseCode());
            	throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
            } 
            
            String line = null;
            while((line = br.readLine()) != null){
                response.append(line);
            }
            br.close();           
            conn.disconnect();
        } catch (IOException e) {
            System.out.println("RestCall Fail : " + e.getMessage());
            return null;
        }
		
		System.out.println(response.toString());
		teams = parseByTeam(response.toString(), token, mattermostUrl, userId);
		
		return teams;
	}
	
	public List<ResRedisTeam> parseByTeam(String body, String token, String mattermostUrl, String userId) {
		// ??? ????????? ????????? List<ResRedisTeam>
		List<ResRedisTeam> teams = new ArrayList<>();
		
		try {
			JSONArray content = (JSONArray) parser.parse(body);		
			// ??? ????????? ??????
			for (int i = 0; i < content.size(); i++) {
				JSONObject temp = (JSONObject) content.get(i);
				ResRedisTeam team = new ResRedisTeam((String)temp.get("id"), (String)temp.get("display_name"), colors[i%colors.length]);
				teams.add(team);		
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return teams;
	}
	
	public List<ResRedisChannel> getChannelsFromMM(String token, String mattermostUrl, String userId, String teamId) {
		String GET_CHANNEL_LIST = mattermostUrl + "/api/v4/users/"+userId+"/teams/"+teamId+"/channels";
		StringBuilder response = new StringBuilder();
		List<ResRedisChannel> channels = new ArrayList<>();
		
		try {
            URL url = new URL(GET_CHANNEL_LIST);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "bearer "+token);
            conn.setRequestProperty("Content-Type", "application/json");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            if (conn.getResponseCode() != 200) {
                System.out.println("Failed: HTTP error code : " + conn.getResponseCode());
            	throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
            } 
            
            String line = null;
            while((line = br.readLine()) != null){
                response.append(line);
            }
            br.close();           
            conn.disconnect();
        } catch (IOException e) {
            System.out.println("RestCall Fail : " + e.getMessage());
        }
		
		System.out.println(response.toString());
		channels = parseByChannel(response.toString());
		
		return channels;
	}
	
	public List<ResRedisChannel> parseByChannel(String body) {
		// ?????? ????????? ????????? List<Channel>
		List<ResRedisChannel> channels = new ArrayList<>();
		
		// ?????? ????????? ??????
		try {
			JSONArray content = (JSONArray) parser.parse(body);
			for (int i = 0; i < content.size(); i++) {
				JSONObject temp = (JSONObject) content.get(i);
				String c = (String) temp.get("type");
				if(c.equals("O") || c.equals("P")) { // ?????????????????? ???????????????
					channels.add(new ResRedisChannel((String)temp.get("id"), (String)temp.get("display_name"), true));
				}	
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return channels;
	}
}
