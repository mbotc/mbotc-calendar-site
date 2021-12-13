package com.ssafy.mbotc.service;

import java.util.Optional;

import com.ssafy.mbotc.entity.Channel;

public interface ChannelService {

	Optional<Channel> findByToken(String token);
}
