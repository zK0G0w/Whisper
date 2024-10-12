package com.zkogow.whisper.config;

import com.zkogow.whisper.resolver.ClientIpArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/10/12 10:00
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private ClientIpArgumentResolver clientIpArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(clientIpArgumentResolver);
    }
}
