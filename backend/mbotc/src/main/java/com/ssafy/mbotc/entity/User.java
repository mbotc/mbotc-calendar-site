package com.ssafy.mbotc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
	
	@ApiModelProperty(example = "kimssafy@ssfay.com")
	@ApiParam(value = "user's email")
	@Column
	String userEmail;
	
	@ApiModelProperty(example = "kimssafy")
	@ApiParam(value = "user's name")
	@Column
	String userName;
	
	@ApiModelProperty(example = "https://your-mattermost-url.com")
	@ApiParam(value = "user's mattermost url")
	@Column
	String url;
	
	@ApiModelProperty(example = "0000000000")
	@ApiParam(value = "user's login auth token")
	@Column
	String token;
	
	@ApiModelProperty(example = "0000000000")
	@ApiParam(value = "user's random id from mattermost API response")
	@Column
	String userId;

	@Override
	public String toString() {
		return "User [userEmail=" + userEmail + ", userName=" + userName + ", url=" + url + ", token=" + token + ", id="
				+ id + "]";
	}
	
}
