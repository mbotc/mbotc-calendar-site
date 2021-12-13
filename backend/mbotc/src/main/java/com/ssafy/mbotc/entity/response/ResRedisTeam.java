package com.ssafy.mbotc.entity.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRedisTeam {

	@ApiModelProperty(example = "000000000")
	@ApiParam(value = "team's random id from mattermost API response")
	String teamId;
	
	@ApiModelProperty(example = "SSAFY 5th Seoul Class 1")
	@ApiParam(value = "team's display name")
	String teamName;
	
	@ApiModelProperty(example = "#FF00FF")
	@ApiParam(value = "team's color by user's setting")
	String color;
	
	@ApiParam(value = "channel list to which team belongs")
	List<ResRedisChannel> subscribe;
	
	@Override
	public String toString() {
		return "{\"teamId\":\"" + teamId + "\", \"teamName\":\"" + teamName + "\", \"color\":\"" + color + "\", \"subscribe\":"
				+ subscribe + "}";
	}

	public ResRedisTeam() {}
	
	public ResRedisTeam(String teamId, String teamName, String color, List<ResRedisChannel> subscribe) {
		super();
		this.teamId = teamId;
		this.teamName = teamName;
		this.color = color;
		this.subscribe = subscribe;
	}

	public ResRedisTeam(String teamId, String teamName, String color) {
		super();
		this.teamId = teamId;
		this.teamName = teamName;
		this.color = color;
	}
	
}
