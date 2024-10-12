package com.zkogow.whisper.service;

import com.zkogow.whisper.model.dto.IpLocationResponse;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/10/12 10:05
 */


public interface IpLocationService {

    /**
     * 获取指定 IP 的地理位置信息
     *
     * @param ip 用户的 IP 地址
     * @return IP 的地理位置信息
     */

    IpLocationResponse getLocationByIp(String ip);

}
