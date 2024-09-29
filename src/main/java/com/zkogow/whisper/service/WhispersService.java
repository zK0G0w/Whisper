package com.zkogow.whisper.service;

import com.zkogow.whisper.entity.Whispers;
import com.zkogow.whisper.model.vo.WhispersVo;

/**
 * @Description:whisper服务层接口
 * @Author: WainZeng
 * @Date: 2024/9/29 11:09
 */


public interface WhispersService {

    WhispersVo getRandomWhispers();
}
