package com.ssafy.mbotc.entity.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRedisUser {

	@ApiParam(value = "team list to which user belongs")
	List<ResRedisTeam> teams;
	
	@ApiModelProperty(example = "light")
	@ApiParam(value = "user's site theme")
	String theme;
	
	@Override
	public String toString() {
		return "{\"teams\":" + teams + ", \"theme\":\"" + theme + "\"}";
	}

	public ResRedisUser() {}
	
	public ResRedisUser(List<ResRedisTeam> teams, String theme) {
		super();
		this.teams = teams;
		this.theme = theme;
	}
	
}
