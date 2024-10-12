package com.zkogow.whisper.entity;

import lombok.*;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 10:45
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Whispers {

    private Long id;
    private String content;
    private Timestamp createdAt;
    private Integer status;

}
