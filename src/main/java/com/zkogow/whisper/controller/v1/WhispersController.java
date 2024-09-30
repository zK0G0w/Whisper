package com.zkogow.whisper.controller.v1;

import com.zkogow.whisper.model.dto.Result;
import com.zkogow.whisper.model.vo.WhispersVo;
import com.zkogow.whisper.service.ImageService;
import com.zkogow.whisper.service.WhispersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 11:43
 */

@Controller
@RequestMapping("/api/v1")
public class WhispersController {

    @Autowired
    private WhispersService whispersService;


    @GetMapping("/whisper")
    @ResponseBody
    public Result<WhispersVo> getRandomWhisper() {
        WhispersVo whispersVo = whispersService.getRandomWhispers();
        if (whispersVo == null) {
            return Result.failure("数据为空");
        } else {
            return Result.success(whispersVo);
        }

    }

}
