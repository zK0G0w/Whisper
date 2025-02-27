package com.zkogow.whisper.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkogow.whisper.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2025/2/27 15:56
 */

@Service
public class ChatServiceImpl implements ChatService {
    @Value("${volc.api.url}")
    private String apiUrl;

    @Value("${volc.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Map<String, Object>> chatHistory = new ArrayList<>();

    @Override
    public String getAnswer(String question) {
        try {
            // 记录用户问题
            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            userMessage.put("timestamp", System.currentTimeMillis());
            chatHistory.add(userMessage);

            // 准备请求体
            Map<String, Object> requestBody = new HashMap<>();
            List<Map<String, String>> messages = new ArrayList<>();

            // 添加历史消息，限制数量以避免超出token限制
            int historyCount = 0;
            for (int i = chatHistory.size() - 1; i >= 0 && historyCount < 10; i--) {
                Map<String, Object> message = chatHistory.get(i);
                if (message.get("role").equals("user") || message.get("role").equals("assistant")) {
                    Map<String, String> msg = new HashMap<>();
                    msg.put("role", (String) message.get("role"));
                    msg.put("content", (String) message.get("content"));
                    messages.add(0, msg);
                    historyCount++;
                }
            }

            requestBody.put("model", "deepseek-r1-250120");
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 2000);
            requestBody.put("temperature", 0.7);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // 发送请求到火山引擎API
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            // 解析回答
            JsonNode root = objectMapper.readTree(response.getBody());
            String answer = root.path("choices").path(0).path("message").path("content").asText();

            // 记录AI回答
            Map<String, Object> aiMessage = new HashMap<>();
            aiMessage.put("role", "assistant");
            aiMessage.put("content", answer);
            aiMessage.put("timestamp", System.currentTimeMillis());
            chatHistory.add(aiMessage);

            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，处理您的请求时发生错误: " + e.getMessage();
        }
    }

    @Override
    public List<Map<String, Object>> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }

    @Override
    public void clearChatHistory() {
        chatHistory.clear();
    }

}
