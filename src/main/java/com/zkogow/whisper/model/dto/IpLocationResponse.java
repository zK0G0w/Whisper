package com.zkogow.whisper.model.dto;

import lombok.*;

/**
 * @Description:
 * @Author: WainZeng
 * @Date: 2024/10/12 10:06
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IpLocationResponse {

    private String status;
    private String country;
    private String city;
    private String isp;
    private String query;


}
