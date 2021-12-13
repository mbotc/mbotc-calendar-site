package com.ssafy.mbotc.entity.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRedisCheckList {
	@Getter
	@Setter
	public class Post{
		String postId;
		boolean isDone;
		
		@Override
		public String toString() {
			return "{\"postId\":\"" + postId + "\", \"isDone\":\"" + isDone + "\"}";
		}
	}
	
	List<Post> posts;

	@Override
	public String toString() {
		return "{\"posts\":" + posts + "\"}";
	}
	
	
}
