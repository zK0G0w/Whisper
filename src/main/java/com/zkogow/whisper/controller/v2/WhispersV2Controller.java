package com.zkogow.whisper.controller.v2;

import com.zkogow.whisper.model.entity.Whispers;
import com.zkogow.whisper.model.dto.Result;
import com.zkogow.whisper.model.vo.WhispersVo;
import com.zkogow.whisper.service.ImageService;
import com.zkogow.whisper.service.WhispersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/9/29 11:43
 */

@Controller
@RequestMapping("/api/v2")
public class WhispersV2Controller {

    @Autowired
    private WhispersService whispersService;

    @Autowired
    private ImageService imageService;


    @GetMapping("/whisper")
    public ResponseEntity<String> getRandomWhisperAsSvg(@RequestHeader("User-Agent") String userAgent) {
        WhispersVo whispers = whispersService.getRandomWhisper();
        String base64Image = imageService.getRandomImage();
        String content = whispers.getContent();
        // 判断是否是 Mac 或 iOS 用户
        boolean isAppleUser = userAgent.toLowerCase().contains("macintosh") ||
                userAgent.toLowerCase().contains("iphone") ||
                userAgent.toLowerCase().contains("ipad");

        //拼接苹果符号
        if (isAppleUser) {
            content = "&#xF8FF; " + content;
        } else {
            String[] symbols = {"&#9775; ", "&#9672; "};
            int randomIndex = (int) (Math.random() * symbols.length); // 随机选择一个符号
            content = symbols[randomIndex] + " " + content;
        }
        String svgContent = "<svg width=\"700\" height=\"50\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">" +
                "<defs>" +
                "<pattern id=\"backgroundPattern\" width=\"680\" height=\"130\" patternUnits=\"userSpaceOnUse\">" +
                "<image xlink:href=\"data:image/png;" + base64Image + "\" width=\"680\" height=\"130\" />" +
                "<animateTransform attributeName=\"patternTransform\" type=\"translate\" from=\"0,0\" to=\"-680,0\" begin=\"0s\" dur=\"30s\" repeatCount=\"indefinite\" />" +
                "</pattern>" +
                "</defs>" +
                "<text x=\"50%\" y=\"50%\" font-size=\"32\" font-weight=\"bold\" text-anchor=\"middle\" fill=\"url(#backgroundPattern)\">" +
                "<tspan x=\"50%\" dy=\"0.3em\">" + content + "</tspan>" +
                "</text>" +
                "</svg>";

        // 设置响应头，确保返回的是 SVG 图片
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/svg+xml");

        // 返回 SVG 内容和 200 状态码
        return new ResponseEntity<>(svgContent, headers, HttpStatus.OK);

    }


    @GetMapping("/list")
    public String getWhisperList(Model model) {
        List<Whispers> list = whispersService.getAllWhispers();
        System.out.println(list);
        //return Result.success(list);
        model.addAttribute("whispers", list);
        return "list";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Result<String> deleteWhisper(@PathVariable Long id) {
        whispersService.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping("/add")
    @ResponseBody
    public Result<String> addWhisper(@RequestBody Whispers whispers) {
        whispersService.insertWhispers(whispers);
        return Result.success("添加成功");
    }

    @GetMapping("403")
    public String accessForbidden() {
        return "403";
    }

}
