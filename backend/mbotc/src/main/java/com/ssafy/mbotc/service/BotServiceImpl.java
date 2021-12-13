package com.ssafy.mbotc.service;

import com.google.gson.Gson;
import com.ssafy.mbotc.entity.request.ReqNoticePost;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service("botService")
public class BotServiceImpl implements BotService {

    @Override
    public void postNoticeToMattermost(ReqNoticePost notice, String mattermostUrl) {
        String CREATE_POST_URL = mattermostUrl + "/plugins/com.mattermost.plugin-mbotc";
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(CREATE_POST_URL);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);
            Gson gson = new Gson();
            byte[] requestBody = gson.toJson(notice).getBytes("utf-8");
            conn.getOutputStream().write(requestBody);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            if (conn.getResponseCode() != 200) {
                System.out.println("Failed: HTTP error code : " + conn.getResponseCode());
                throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
            }

            String line = null;
            while((line = br.readLine()) != null){
                response.append(line);
            }
            br.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.toString());
    }

}
