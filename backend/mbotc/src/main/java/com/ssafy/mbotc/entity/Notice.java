package com.ssafy.mbotc.entity;

import java.util.Date;

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
@Table(name = "notice")
public class Notice extends BaseEntity {
	
	@ApiParam(value = "channel to which notice belongs")
	@ManyToOne(cascade = CascadeType.MERGE)
	Channel channel;
	
	@ApiParam(value = "user who write this notice")
	@ManyToOne(cascade = CascadeType.MERGE)
	User user;
	
	@ApiModelProperty(example = "2021-01-01 00:00:00.000000")
	@ApiParam(value = "time when this notice written")
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date time;
	
	@ApiModelProperty(example = "hello, this is cool.")
	@ApiParam(value = "notice's content")
	@Column(columnDefinition = "TEXT")
	String content;
	
	@ApiModelProperty(example = "2021-01-01 00:00:00.000000")
	@ApiParam(value = "start period of notice")
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date startTime;
	
	@ApiModelProperty(example = "2021-01-01 00:00:00.000000")
	@ApiParam(value = "end period of notice")
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date endTime;
	
	@ApiModelProperty(example = "0000000000")
	@ApiParam(value = "files' id which this notice has")
	@Column
	String files;
	
	@ApiModelProperty(example = "0000000000")
	@ApiParam(value = "notice's random id from mattermost API response")
	@Column
	String token;

}
