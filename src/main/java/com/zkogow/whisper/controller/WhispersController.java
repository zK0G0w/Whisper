package com.zkogow.whisper.controller;

import com.zkogow.whisper.model.dto.Result;
import com.zkogow.whisper.model.vo.WhispersVo;
import com.zkogow.whisper.service.ImageService;
import com.zkogow.whisper.service.WhispersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 11:43
 */

@Controller
@RequestMapping("/api")
public class WhispersController {

    @Autowired
    private WhispersService whispersService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/v1/whisper")
    @ResponseBody
    public Result<WhispersVo> getRandomWhisper() {
        WhispersVo whispersVo = whispersService.getRandomWhispers();
        if (whispersVo == null) {
            return Result.failure("数据为空");
        } else {
            return Result.success(whispersVo);
        }

    }

    @GetMapping("/v2/whisper")
    public ResponseEntity<String> getRandomWhisperAsSvg() {
        WhispersVo whispers = whispersService.getRandomWhispers();
        String content = whispers.getContent();
        String base64Image = imageService.getRandomImage();
        String svgContent = "<svg width=\"700\" height=\"50\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">" +
                "<defs>" +
                "<pattern id=\"backgroundPattern\" width=\"680\" height=\"130\" patternUnits=\"userSpaceOnUse\">" +
                "<image xlink:href=\"data:image/png;" + base64Image + "\" width=\"680\" height=\"130\" />" +
                "<animateTransform attributeName=\"patternTransform\" type=\"translate\" from=\"0,0\" to=\"-680,0\" begin=\"0s\" dur=\"30s\" repeatCount=\"indefinite\" />" +
                "</pattern>" +
                "</defs>" +
                "<text x=\"50%\" y=\"50%\" font-size=\"32\" font-weight=\"bold\" text-anchor=\"middle\" fill=\"url(#backgroundPattern)\">" +
                "<tspan x=\"50%\" dy=\"0.3em\">" + "&#xF8FF;" + content + "</tspan>" +
                "</text>" +
                "</svg>";

        // 设置响应头，确保返回的是 SVG 图片
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/svg+xml");

        // 返回 SVG 内容和 200 状态码
        return new ResponseEntity<>(svgContent, headers, HttpStatus.OK);

    }
}
