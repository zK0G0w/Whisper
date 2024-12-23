package com.zkogow.whisper.controller.v2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/12/23 15:38
 */

@Controller
@RequestMapping("/api/v2")
public class ChatGPTController {

    @GetMapping("/chatgpt")
    public String getChatGPT() {
        return "redirect:https://llm.zkogow.cn/ui/chat/67be5d915b2f0bad";
    }
}
