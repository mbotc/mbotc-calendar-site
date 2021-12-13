package com.ssafy.mbotc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mbotc.dao.ChannelRepository;
import com.ssafy.mbotc.entity.Channel;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService {

	@Autowired
	ChannelRepository channelRepository;

	@Override
	public Optional<Channel> findByToken(String token) {
		return channelRepository.findByToken(token);
	}
	
	

}
