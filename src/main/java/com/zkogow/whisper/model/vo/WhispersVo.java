package com.zkogow.whisper.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @Description:WhispersVo
 * @Author: WainZeng
 * @Date: 2024/9/29 14:36
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WhispersVo {

    private String content;
    private Timestamp createdAt;


}
