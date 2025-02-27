package com.zkogow.whisper.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkogow.whisper.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseExtractor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelOption;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2025/2/27 15:56
 */

@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Value("${volc.api.url}")
    private String apiUrl;

    @Value("${volc.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Map<String, Object>> chatHistory = new ArrayList<>();
    private final WebClient webClient;

    public ChatServiceImpl() {
        // 配置 HttpClient
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)  // 连接超时30秒
                .compress(true)  // 启用压缩
                .wiretap(true);  // 启用调试日志

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

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

    @Override
    public void getStreamAnswer(String question, Consumer<String> callback) {
        try {
            log.info("开始处理流式回答请求: {}", question);
            
            // 准备请求体，完全匹配curl示例的格式
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-r1-250120");
            requestBody.put("stream", true);
            
            List<Map<String, String>> messages = new ArrayList<>();
            
            // 系统消息
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是Deepseek R1模型");
            messages.add(systemMessage);
            
            // 用户消息
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messages.add(userMessage);
            
            requestBody.put("messages", messages);
            
            // 记录到历史
            Map<String, Object> historyMessage = new HashMap<>();
            historyMessage.put("role", "user");
            historyMessage.put("content", question);
            historyMessage.put("timestamp", System.currentTimeMillis());
            chatHistory.add(historyMessage);

            log.info("发送请求，URL: {}, 请求体: {}", apiUrl, objectMapper.writeValueAsString(requestBody));

            StringBuilder fullAnswer = new StringBuilder();
            StringBuilder reasoningContent = new StringBuilder();
            
            webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToFlux(String.class)
                    .doOnSubscribe(subscription -> log.info("开始订阅流式响应"))
                    .subscribe(chunk -> {
                        try {
                            log.info("接收到数据块: {}", chunk);
                            JsonNode root = objectMapper.readTree(chunk);
                            JsonNode deltaNode = root.path("choices")
                                    .path(0)
                                    .path("delta");
                            
                            log.info("解析的delta节点: {}", deltaNode);
                            
                            // 获取 reasoning_content
                            String reasoning = deltaNode.path("reasoning_content").asText("");
                            if (!reasoning.isEmpty()) {
                                log.info("提取到思考内容: {}", reasoning);
                                reasoningContent.append(reasoning);
                                callback.accept("thinking:" + formatMarkdown(reasoning));
                            }
                            
                            // 获取 content
                            String content = deltaNode.path("content").asText("");
                            if (!content.isEmpty()) {
                                log.info("提取到回答内容: {}", content);
                                fullAnswer.append(content);
                                callback.accept("answer:" + formatMarkdown(content));
                            }
                            
                        } catch (Exception e) {
                            // 如果解析失败，可能是[DONE]标记
                            if (!"[DONE]".equals(chunk)) {
                                log.error("解析JSON数据失败: {}", e.getMessage(), e);
                                callback.accept("error:" + e.getMessage());
                            } else {
                                log.info("收到结束标记");
                            }
                        }
                    }, error -> {
                        log.error("处理流式响应时发生错误: {}", error.getMessage(), error);
                        callback.accept("error:" + error.getMessage());
                    }, () -> {
                        String reasoning = reasoningContent.toString().trim();
                        String answer = fullAnswer.toString().trim();
                        
                        log.info("完成流式响应处理");
                        log.info("累积的思考过程: {}", reasoning);
                        log.info("累积的最终回答: {}", answer);
                        
                        if (!reasoning.isEmpty() || !answer.isEmpty()) {
                            log.info("保存回答到历史记录");
                            // 记录完整回答
                            Map<String, Object> aiMessage = new HashMap<>();
                            aiMessage.put("role", "assistant");
                            aiMessage.put("content", answer);
                            aiMessage.put("reasoning", reasoning);
                            aiMessage.put("timestamp", System.currentTimeMillis());
                            chatHistory.add(aiMessage);
                        } else {
                            log.warn("未收到有效的响应内容");
                        }
                    });

        } catch (Exception e) {
            log.error("处理请求时发生异常: {}", e.getMessage(), e);
            callback.accept("error:" + e.getMessage());
        }
    }

    // 添加格式化方法
    private String formatMarkdown(String content) {
        // 确保代码块正确格式化
        content = content.replaceAll("```(\\w+)?\\s*\\n", "```$1\n");
        
        // 确保列表格式正确
        content = content.replaceAll("(?m)^-\\s", "- ");
        content = content.replaceAll("(?m)^\\d+\\.\\s", "$0 ");
        
        // 确保标题格式正确
        content = content.replaceAll("(?m)^#\\s", "# ");
        
        return content;
    }
}
