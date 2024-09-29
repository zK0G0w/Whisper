package com.zkogow.whisper.service.impl;

import com.zkogow.whisper.mapper.ImageMapper;
import com.zkogow.whisper.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 16:26
 */

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public String getRandomImage() {
        int count = imageMapper.countImages();
         if (count == 0) {
            return null;
        }

        // 生成一个随机偏移量
        Random random = new Random();
        int randomOffset = random.nextInt(count);  // 生成 [0, count-1] 范围内的随机数
        return imageMapper.findImageByOffset(randomOffset);
    }
}
