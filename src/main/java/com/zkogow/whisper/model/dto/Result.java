package com.zkogow.whisper.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 14:13
 */

@Getter
@Setter
@NoArgsConstructor
public class Result<T> {

    private int status;        // 状态码
    private String message;    // 响应信息
    private T data;            // 响应数据
    private LocalDateTime timestamp; // 时间戳

    public Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();  // 设置当前时间戳
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(500, message, null);
    }

}
