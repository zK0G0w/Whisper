package com.zkogow.whisper.controller.v1;

import com.zkogow.whisper.model.dto.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/10/6 08:47
 */

@RestController
@RequestMapping("/api/vi")
public class AutoJsV1Controller {
    // 定义接收状态信息的 API
    @PostMapping("/autojs")
    public ResponseEntity<Result<String>> receiveStatus(@RequestBody StatusRequest statusRequest) {
        // 打印接收到的信息
        System.out.println("状态: " + statusRequest.getStatus());
        System.out.println("信息: " + statusRequest.getMessage());
        System.out.println("时间: " + statusRequest.getTimestamp());

        // 你可以将日志存储到数据库或其他持久化存储
        // 返回自定义的 Result 响应对象
        return ResponseEntity.ok(Result.success("状态接收成功"));
    }

    // StatusRequest作为内部类，用于接收JSON数据
    public static class StatusRequest {
        private String status;
        private String message;
        private String timestamp;

        // Getters 和 Setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }


}
