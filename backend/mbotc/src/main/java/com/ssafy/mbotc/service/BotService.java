package com.ssafy.mbotc.service;

import com.ssafy.mbotc.entity.request.ReqNoticePost;

public interface BotService {
    void postNoticeToMattermost(ReqNoticePost notice, String mattermostUrl);
}
