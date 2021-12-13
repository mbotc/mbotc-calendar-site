package com.ssafy.mbotc.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class Channel extends BaseEntity {
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@ApiParam(value = "team to which channel belongs")
	Team team;
	
	@ApiModelProperty(example = "Q&A")
	@ApiParam(value = "channel's name")
	@Column
	String name;
	
	@ApiModelProperty(example = "0000000000")
	@ApiParam(value = "channel's random id from mattermost API response")
	@Column
	String token;

	public Channel() {}
	
	public Channel(Team team, String name, String token) {
		super();
		this.team = team;
		this.name = name;
		this.token = token;
	}

	
}
