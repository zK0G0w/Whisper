package com.zkogow.whisper.controller.v2;

import com.zkogow.whisper.model.dto.IpLocationResponse;
import com.zkogow.whisper.service.IpLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v2")
public class IpController {

    @Autowired
    private IpLocationService ipLocationService;

    @GetMapping("/ipinfo")
    public String getIpInfo(HttpServletRequest request, Model model) {
        // 获取用户真实 IP 地址（未使用代理时的 IP）
        String realIp = getClientIp(request);
        IpLocationResponse realLocation = ipLocationService.getLocationByIp(realIp);

        // 将真实 IP 和地理位置信息传递给前端页面
        model.addAttribute("realIp", realIp);
        model.addAttribute("realLocation", realLocation);

        // 返回页面
        return "ipInfo";
    }

    // 获取客户端的真实 IP 地址
    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    @GetMapping("/proxy/ipinfo")
    public ResponseEntity<String> proxyIpInfo(@RequestParam String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://ip-api.com/json/" + ip;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response;
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching IP info", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}