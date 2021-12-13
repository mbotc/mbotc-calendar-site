package com.ssafy.mbotc.entity.response;

import java.util.List;

import com.ssafy.mbotc.entity.Notice;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResNoticeList {

	@ApiModelProperty(example = "channelId1, channelId2, channelId3")
	@ApiParam(value = "channels ID you are subscribed to")
	String subscribe;
	
	@ApiParam(value = "notice")
	List<Notice> notifications;
	
}
