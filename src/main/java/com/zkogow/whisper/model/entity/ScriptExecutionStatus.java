package com.zkogow.whisper.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 用于记录打卡脚本的实体类
 * @Author: WainZeng
 * @Date: 2024/10/6 09:07
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScriptExecutionStatus {

    private Long id;
    private String status;
    private String message;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
