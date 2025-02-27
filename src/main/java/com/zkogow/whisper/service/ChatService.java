package com.zkogow.whisper.service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2025/2/27 15:56
 */

public interface ChatService {

    /**
     * 获取AI模型的回答
     * @param question 用户问题
     * @return AI回答
     */
    String getAnswer(String question);

    /**
     * 获取聊天历史记录
     * @return 聊天历史列表
     */
    List<Map<String, Object>> getChatHistory();

    /**
     * 清空聊天历史
     */
    void clearChatHistory();

}
