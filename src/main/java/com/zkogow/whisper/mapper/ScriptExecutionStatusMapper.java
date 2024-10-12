package com.zkogow.whisper.mapper;

import com.zkogow.whisper.entity.ScriptExecutionStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ScriptExecutionStatusMapper {

    @Insert("INSERT INTO script_execution_status (status, message, start_time, end_time) " +
            "VALUES (#{status}, #{message}, #{startTime}, #{endTime})")
    void insertScriptStatus(ScriptExecutionStatus status);

    @Select("SELECT id, status, message, start_time AS startTime, end_time AS endTime " +
            "FROM script_execution_status ORDER BY start_time DESC")
    List<ScriptExecutionStatus> findAllScriptStatuses();

    @Select("SELECT id, status, message, start_time AS startTime, end_time AS endTime " +
            "FROM script_execution_status ORDER BY start_time DESC LIMIT 1")
    ScriptExecutionStatus findLatestTask();

    @Update("UPDATE script_execution_status SET status = #{status}, message = #{message}, end_time = #{endTime} " +
            "WHERE id = #{id}")
    void updateScriptStatus(ScriptExecutionStatus status);
}