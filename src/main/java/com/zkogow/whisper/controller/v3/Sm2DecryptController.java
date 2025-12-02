package com.zkogow.whisper.controller.v3;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.zkogow.whisper.model.dto.Result;
import com.zkogow.whisper.model.dto.Sm2DecryptRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2025/12/2 11:07
 */

@Slf4j
@RestController
@RequestMapping("/api/v3")
public class Sm2DecryptController {

    /**
     * SM2解密接口
     * @param request 包含密文和私钥的请求参数
     * @return 解密后的明文
     */
    @PostMapping("/sm2/decrypt")
    public Result<String> sm2Decrypt(@RequestBody Sm2DecryptRequest request) {
        try {
            // 参数校验
            if (request.getCipherText() == null || request.getCipherText().trim().isEmpty()) {
                return Result.failure("密文不能为空");
            }
            if (request.getPrivateKey() == null || request.getPrivateKey().trim().isEmpty()) {
                return Result.failure("私钥不能为空");
            }
            
            // 创建SM2实例,使用私钥
            SM2 sm2 = new SM2(request.getPrivateKey(), null);
            
            // 解密(密文为十六进制字符串)
            String decryptedText = sm2.decryptStr(request.getCipherText(), KeyType.PrivateKey);
            
            log.info("SM2解密成功");
            return Result.success(decryptedText);
            
        } catch (Exception e) {
            log.error("SM2解密失败: {}", e.getMessage(), e);
            return Result.failure("解密失败: " + e.getMessage());
        }
    }
}
