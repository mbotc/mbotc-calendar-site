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
@Table(name = "team")
public class Team extends BaseEntity {
	
	@Column
	@ApiModelProperty(example = "SSAFY 5th Seoul Class 1")
	@ApiParam(value = "team's display name")
	String name;
	
	@Column
	@ApiModelProperty(example = "0000000000")
	@ApiParam(value = "team's random id from mattermost API response")
	String token;

	public Team() {}
	
	public Team(String name, String token) {
		super();
		this.name = name;
		this.token = token;
	}

}
