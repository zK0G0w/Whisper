package com.zkogow.whisper.service.impl;

import com.zkogow.whisper.entity.Whispers;
import com.zkogow.whisper.mapper.WhispersMapper;
import com.zkogow.whisper.model.dto.Result;
import com.zkogow.whisper.model.vo.WhispersVo;
import com.zkogow.whisper.service.WhispersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 11:39
 */

@Service
public class WhispersServiceImpl implements WhispersService {

    @Autowired
    private WhispersMapper whispersMapper;

    @Override
    public WhispersVo getRandomWhisper() {
        // 获取表中的记录数
        int count = whispersMapper.countAll();
        if (count == 0) {
            return null;
        }

        // 生成一个随机偏移量
        Random random = new Random();
        int randomOffset = random.nextInt(count);  // 生成 [0, count-1] 范围内的随机数
        Whispers whispers = whispersMapper.findByOffset(randomOffset);
        return new WhispersVo(whispers.getContent(), whispers.getCreatedAt());

    }

    @Override
    public List<Whispers> getAllWhispers() {
        List<Whispers> list = whispersMapper.findAll();
        return list;
    }

    @Override
    public void deleteById(Long id) {
        whispersMapper.deleteById(id);
    }

    @Override
    public void insertWhispers(Whispers whispers) {
        whispersMapper.insertWhispers(whispers);
    }
}
