package com.me.stdio.service;

import com.me.stdio.model.UserInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的 MCP 服务类
 *
 * @ClassName: UserService
 * @Author: JacksonLee
 * @Date: 2026/10/26
 */
@Service
public class UserService {

    static Map<String, UserInfo> map = new HashMap<>();

    static {
        map.put("Zhangsan", new UserInfo("Zhangsan", 18, "男", "Beijing"));
        map.put("Rachel", new UserInfo("Rachel", 22, "女", "California"));
        map.put("Wangwu", new UserInfo("Wangwu", 28, "男", "Shanghai"));
        map.put("Bruce", new UserInfo("Bruce", 23, "男", "New York"));
        map.put("Nancy", new UserInfo("Nancy", 21, "女", "Indiana"));
    }

    /**
     * MCP 工具方法：根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息字符串
     * @throws IllegalArgumentException 如果用户名不存在或为空
     */
    @Tool(description = "根据用户名查询用户信息")
    public String getUserInfo(String username) {
        if (map.containsKey(username)) {
            return map.get(username).toString();
        }
        return "不存在该用户";
    }
}
