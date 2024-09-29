package com.zkogow.whisper.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 16:26
 */

@Mapper
public interface ImageMapper {


    @Select("SELECT count(*) FROM image")
    int countImages();

    @Select("SELECT base64_data AS base64Data FROM image LIMIT 1 OFFSET #{offset}")
    String findImageByOffset(@Param("offset") int offset);
}
