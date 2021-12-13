package com.ssafy.mbotc.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value="notice content", description = "notice content needs for create mm post")
public class ReqNoticePost {

    @ApiModelProperty(name = "user name who want to notice")
    String user_name;

    @ApiModelProperty(name = "notice's content")
    String message;

    @ApiModelProperty(name = "schedule start date&time (YYYY-MM-DD hh:mm)")
    String start_time;

    @ApiModelProperty(name = "schedule end date&time (YYYY-MM-DD hh:mm)")
    String end_time;

    @ApiModelProperty(name = "team name to notice")
    String team_name;
    
    @ApiModelProperty(name = "channel name to notice")
    String channel_name;

    public ReqNoticePost() {}
    
	public ReqNoticePost(String user_name, String message, String start_time, String end_time, String team_name,
			String channel_name) {
		super();
		this.user_name = user_name;
		this.message = message;
		this.start_time = start_time;
		this.end_time = end_time;
		this.team_name = team_name;
		this.channel_name = channel_name;
	}

    
}
