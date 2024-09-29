package com.zkogow.whisper.mapper;

import com.zkogow.whisper.entity.Whispers;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 10:54
 */

@Mapper
public interface WhispersMapper {

    @Insert("INSERT INTO whispers(content, created_at, status) VALUES (#{content}, #{createdAt}, #{status})")
    void insertWhispers(Whispers whispers);

    //获取总数
    @Select("SELECT COUNT(*) FROM whispers")
    int countWhispers();

    // 根据偏移量获取一条随机记录
    @Select("SELECT id, content, created_at AS createdAt, status FROM whispers LIMIT 1 OFFSET #{offset}")
    Whispers findWhisperByOffset(@Param("offset") int offset);

    @Select("SELECT * FROM whispers WHERE id = #{id}")
    Whispers findWhispersById(Long id);

    //@Select("SELECT * FROM whispers LIMIT 1 OFFSET FLOOR(RAND() * (SELECT COUNT(*) FROM whispers))")
    //Whispers findRandomWhispers();

    @Select("SELECT * FROM whispers")
    List<Whispers> findAllWhispers();

    @Update("UPDATE whispers SET content = #{content}, status = #{status}")
    void updateWhispers(Whispers whispers);

    @Delete("DELETE FROM whispers WHERE id = #{id}")
    void deleteWhispers(Long id);
}
