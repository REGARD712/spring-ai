package com.me.sse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户信息类
 *
 * @ClassName: UserInfo
 * @Author: JacksonLee
 * @Date: 2026/10/26
 */
@Data
@AllArgsConstructor
public class UserInfo {
    private String name;
    private Integer age;
    private String gender;
    private String address;
}
