package com.zkogow.whisper.controller.v1;

import com.zkogow.whisper.mapper.ScriptExecutionStatusMapper;
import com.zkogow.whisper.entity.ScriptExecutionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/api/v1")
public class ScriptMonitorController {

    @Autowired
    private ScriptExecutionStatusMapper statusMapper;

    private boolean taskAvailable = false;

    // 设置任务并记录任务开始时间
    @PostMapping("/setTask")
    @ResponseBody
    public Map<String, String> setTask() {
        taskAvailable = true;  // 设置任务标志
        ScriptExecutionStatus status = new ScriptExecutionStatus();
        status.setStatus("IN_PROGRESS");
        status.setMessage("任务已开始");
        status.setStartTime(LocalDateTime.now());  // 记录任务开始时间
        statusMapper.insertScriptStatus(status);   // 保存任务状态到数据库

        Map<String, String> response = new HashMap<>();
        response.put("message", "任务已设置。");
        return response;
    }

    // 手机端在任务成功执行后发送确认，并记录任务结束时间
    @PostMapping("/taskCompleted")
    @ResponseBody
    public ResponseEntity<String> taskCompleted(@RequestBody Map<String, String> payload) {
        taskAvailable = false;  // 重置任务状态
        String statusMessage = payload.get("message");
        String taskStatus = payload.get("status");

        // 查找最新任务并更新状态和结束时间
        ScriptExecutionStatus latestStatus = statusMapper.findLatestTask();
        if (latestStatus != null) {
            latestStatus.setEndTime(LocalDateTime.now());  // 记录任务结束时间
            latestStatus.setStatus(taskStatus);  // 更新任务状态为"成功"或"失败"
            latestStatus.setMessage(statusMessage);
            statusMapper.updateScriptStatus(latestStatus);  // 更新任务状态
        }

        return ResponseEntity.ok("任务已完成");
    }

    // 手机端轮询检查任务的逻辑
    @GetMapping("/checkTask")
    @ResponseBody
    public Map<String, String> checkTask() {
        Map<String, String> response = new HashMap<>();
        if (taskAvailable) {
            response.put("task", "executeScript");  // 如果任务可用，返回 executeScript
        } else {
            response.put("task", "none");  // 任务完成或不可用时，返回 none
        }
        return response;
    }

    // 获取所有状态，用于API调用
    @GetMapping("/status")
    @ResponseBody
    public List<ScriptExecutionStatus> getAllStatuses() {
        return statusMapper.findAllScriptStatuses();
    }

    // 用于渲染监控页面
    @GetMapping("/monitor")
    public String monitorScripts(Model model) {
        List<ScriptExecutionStatus> scriptStatuses = statusMapper.findAllScriptStatuses();
        model.addAttribute("scriptStatuses", scriptStatuses);
        return "monitor";  // 返回Thymeleaf模板页面monitor.html
    }

    // 渲染触发页面
    @GetMapping("/trigger")
    public String showTriggerPage() {
        return "trigger";
    }

    // 渲染聚合页面
    @GetMapping("/fyc")
    public String showDeliScriptPage() {
        return "deliscript";
    }
}