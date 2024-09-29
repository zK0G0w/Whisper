package com.zkogow.whisper.controller;

import com.zkogow.whisper.entity.Whispers;
import com.zkogow.whisper.model.dto.Result;
import com.zkogow.whisper.model.vo.WhispersVo;
import com.zkogow.whisper.service.WhispersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 11:43
 */

@RestController
@RequestMapping("/api")
public class WhispersController {

    @Autowired
    private WhispersService whispersService;

    @GetMapping("/v1/whisper")
    public Result<WhispersVo> getRandomWhisper() {
        WhispersVo whispersVo = whispersService.getRandomWhispers();
        if (whispersVo == null) {
            return Result.failure("数据为空");
        } else {
            return Result.success(whispersVo);
        }


    }
}
