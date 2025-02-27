package com.zkogow.whisper.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 16:11
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    private Integer id;
    private String base64Data;
}
