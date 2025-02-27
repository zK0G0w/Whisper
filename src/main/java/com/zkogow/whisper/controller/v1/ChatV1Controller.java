package com.zkogow.whisper.controller.v1;

import com.zkogow.whisper.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2025/2/27 15:55
 */

@Controller
@RequestMapping("/api/v1")
public class ChatV1Controller {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public String index(Model model) {
        model.addAttribute("messages", new ArrayList<>());
        return "deepseekchat";
    }

    @PostMapping("/ask")
    @ResponseBody
    public Map<String, Object> ask(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String answer = chatService.getAnswer(question);

        Map<String, Object> response = new HashMap<>();
        response.put("answer", answer);
        response.put("status", "success");

        return response;
    }

    @GetMapping("/chat-history")
    @ResponseBody
    public List<Map<String, Object>> getChatHistory() {
        return chatService.getChatHistory();
    }

    @PostMapping("/clear-chat")
    @ResponseBody
    public Map<String, Object> clearChatHistory() {
        chatService.clearChatHistory();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Chat history cleared");

        return response;
    }

}
