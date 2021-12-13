package com.ssafy.mbotc.entity.response;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRedisChannel {

	@ApiModelProperty(example = "000000000")
	@ApiParam(value = "channel's random id from mattermost API response")
	String channelId;
	
	@ApiModelProperty(example = "Q&A")
	@ApiParam(value = "channel's name\"")
	String channelName;
	
	@ApiModelProperty(example = "true")
	@ApiParam(value = "subscribe to the channel")
	boolean show;
	
	@Override
	public String toString() {
		return "{\"channelId\":\"" + channelId + "\", \"channelName\":\"" + channelName + "\", \"show\":\"" + show + "\"}";
	}

	public ResRedisChannel() {}

	public ResRedisChannel(String channelId, String channelName, boolean show) {
		super();
		this.channelId = channelId;
		this.channelName = channelName;
		this.show = show;
	}
}
