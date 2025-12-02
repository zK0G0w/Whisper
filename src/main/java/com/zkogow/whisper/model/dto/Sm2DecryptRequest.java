package com.zkogow.whisper.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: SM2解密请求参数
 * @Author: WainZeng
 * @Date: 2025/12/2 11:07
 */
@Getter
@Setter
public class Sm2DecryptRequest {
    
    /**
     * 密文(十六进制字符串)
     */
    private String cipherText;
    
    /**
     * 私钥(十六进制字符串)
     */
    private String privateKey;
}
