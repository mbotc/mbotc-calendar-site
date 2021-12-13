package com.ssafy.mbotc.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mbotc.dao.ChannelRepository;
import com.ssafy.mbotc.dao.NoticeRepository;
import com.ssafy.mbotc.entity.Notice;
import com.ssafy.mbotc.entity.request.ReqNoticePost;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	ChannelRepository channelRepository;

	@Override
	public List<Notice> getNoticeByYearAndMonth(String year, String month, List<Long> channelId) {
		return noticeRepository.findAllByYearAndMonth(year+"-"+month, channelId);
	}

	@Override
	public Notice findByNoticeId(String postId) {
		return noticeRepository.findByToken(postId);
	}

	@Override
	public List<Notice> getNoticeByYearAndMonthAndDay(String year, String month, String day, List<Long> channelId) {
		//long channelId = channelRepository.findByToken(channelToken).get().getId();
		return noticeRepository.findAllByYearAndMonthAndDay(year+"-"+month+"-"+day, channelId);
	}
	
	@Override
	public Notice save(Notice notice) {
		return noticeRepository.save(notice);
	}

	@Override
	public List<ReqNoticePost> getTodayNoticeList(List<Long> channelId) {
//		long channelId = channelRepository.findByToken(channelToken).get().getId();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		String formatedNow = df.format(new Date());
		//System.out.println(formatedNow);
		List<Notice> notices = noticeRepository.findAllByYearAndMonthAndDay(formatedNow, channelId);
		
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK);
		List<ReqNoticePost> response = new ArrayList<>();
		for (int i = 0; i < notices.size(); i++) {
			Notice n = notices.get(i);
			String startT = df.format(n.getStartTime());
			String endT = df.format(n.getEndTime());
			response.add(new ReqNoticePost(n.getUser().getUserName(), n.getContent(), startT, endT, n.getChannel().getName(),n.getChannel().getTeam().getName()));
		}
		return response;
	}

	@Override
	public void deleteByToken(String postId) {
		Notice notice = noticeRepository.findByToken(postId);
		noticeRepository.delete(notice);
		
	}

	@Override
	public List<Notice> getNoticeSearch(String word, List<Long> channelId) {
		List<Notice> resultNotices = noticeRepository.findAllByContentContainingAndChannelIdIn(word, channelId);
		return resultNotices;
	}

//
//	@Override
//	public String uploadFileToMM(MultipartFile file, String mattermostUrl, String token, String channelId) {
//		String GET_TEAM_LIST = mattermostUrl + "/api/v4/files";
//		StringBuilder response = new StringBuilder();
//		
//		try {
//            URL url = new URL(GET_TEAM_LIST);
//            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", "bearer "+token);
//            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=^-----^");
//            conn.setDoOutput(true);
//            
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("channel_id", channelId);
//            jsonObject.put("filename", file);
//            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
//            osw.write(jsonObject.toString());
//            osw.flush();
//            osw.close();
//            
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
//            if (conn.getResponseCode() != 200) {
//                System.out.println("Failed: HTTP error code : " + conn.getResponseCode());
//            	throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
//            } 
//            
//            String line = null;
//            while((line = br.readLine()) != null){
//                response.append(line);
//            }
//            br.close();           
//            conn.disconnect();
//        } catch (IOException e) {
//            System.out.println("RestCall Fail : " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//		
//		System.out.println(response.toString());
//
//		return null;
//	}

}
