package com.zkogow.whisper.controller.v1;

import com.zkogow.whisper.model.dto.Result;
import com.zkogow.whisper.model.vo.WhispersVo;
import com.zkogow.whisper.service.WhispersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 11:43
 */

@Controller
@RequestMapping("/api/v1")
public class WhispersV1Controller {

    @Autowired
    private WhispersService whispersService;


    @GetMapping("/whisper")
    @ResponseBody
    public Result<WhispersVo> getRandomWhisper() {
        WhispersVo whispersVo = whispersService.getRandomWhisper();
        if (whispersVo == null) {
            return Result.failure("数据为空");
        } else {
            return Result.success(whispersVo);
        }

    }

}
