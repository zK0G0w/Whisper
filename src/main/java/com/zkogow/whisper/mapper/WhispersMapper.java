package com.zkogow.whisper.mapper;

import com.zkogow.whisper.model.entity.Whispers;
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
    int insertWhispers(Whispers whispers);

    //获取总数
    @Select("SELECT COUNT(*) FROM whispers")
    int countAll();

    // 根据偏移量获取一条随机记录
    @Select("SELECT id, content, created_at AS createdAt, status FROM whispers LIMIT 1 OFFSET #{offset}")
    Whispers findByOffset(@Param("offset") int offset);

    @Select("SELECT * FROM whispers WHERE id = #{id}")
    Whispers findById(Long id);

    //@Select("SELECT * FROM whispers LIMIT 1 OFFSET FLOOR(RAND() * (SELECT COUNT(*) FROM whispers))")
    //Whispers findRandomWhispers();

    @Select("SELECT * FROM whispers")
    List<Whispers> findAll();

    @Update("UPDATE whispers SET content = #{content}, status = #{status} WHERE id = #{id}")
    void updateWhispers(Whispers whispers);

    @Delete("DELETE FROM whispers WHERE id = #{id}")
    void deleteById(Long id);

    @Select("SELECT status FROM whispers WHERE id = #{id}")
    Integer getStatusById(Long id);
}
