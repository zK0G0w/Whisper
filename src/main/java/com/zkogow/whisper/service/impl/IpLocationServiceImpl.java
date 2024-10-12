package com.zkogow.whisper.service.impl;

import com.zkogow.whisper.model.dto.IpLocationResponse;
import com.zkogow.whisper.service.IpLocationService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 实现 IpLocationService 接口，用于获取 IP 地址的地理位置信息
 * @Author: WainZeng
 * @Date: 2024/10/12 10:05
 */

@Service
public class IpLocationServiceImpl implements IpLocationService {

    private static final String API_URL = "http://ip-api.com/json/";

    @Override
    public IpLocationResponse getLocationByIp(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + ip;  // 拼接查询 URL
        System.out.println("查询的 IP 地址: " + ip);

        try {
            // 设置 User-Agent，避免请求被拦截
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");  // 模拟浏览器请求
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 发起请求，获取地理位置信息
            ResponseEntity<IpLocationResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, IpLocationResponse.class);

            // 处理返回的数据，确保字段不为空
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && "success".equalsIgnoreCase(response.getBody().getStatus())) {
                IpLocationResponse body = response.getBody();
                body.setCountry(body.getCountry() != null ? body.getCountry() : "未知");
                body.setCity(body.getCity() != null ? body.getCity() : "未知");
                body.setIsp(body.getIsp() != null ? body.getIsp() : "未知");
                body.setQuery(body.getQuery() != null ? body.getQuery() : "未知");
                return body;
            } else {
                System.out.println("IP 地址查询失败，状态: " + (response.getBody() != null ? response.getBody().getStatus() : "未知"));
                return new IpLocationResponse("fail", "未知", "未知", "未知", ip);
            }
        } catch (Exception e) {
            System.out.println("获取 IP 地址地理位置信息时发生异常: " + e.getMessage());
            e.printStackTrace();
            return new IpLocationResponse("error", "未知", "未知", "未知", ip);
        }
    }
}