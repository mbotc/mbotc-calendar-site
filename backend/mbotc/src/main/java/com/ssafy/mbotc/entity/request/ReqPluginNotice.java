package com.ssafy.mbotc.entity.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqPluginNotice {
	String user_id;
	String message;
	String start_time;
	String end_time;
	List<String> file_ids;
	String channel_id;
	String post_id;
	
}
