package com.zkogow.whisper.service;

import com.zkogow.whisper.entity.Whispers;
import com.zkogow.whisper.model.vo.WhispersVo;

import java.util.List;

/**
 * @Description:whisper服务层接口
 * @Author: WainZeng
 * @Date: 2024/9/29 11:09
 */


public interface WhispersService {

    WhispersVo getRandomWhisper();

    List<Whispers> getAllWhispers();

    void deleteById(Long id);

    void insertWhispers(Whispers whispers);
}
