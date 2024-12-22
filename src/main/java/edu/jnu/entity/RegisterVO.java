package edu.jnu.entity;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月01日 18时39分
 * @功能描述: 账户注册类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterVO {
    private int id;
    private String name;
    private String password;
    private String role;
    public RegisterVO(JSONObject jsonObject) {
        this.id = jsonObject.getIntValue("id",0);
        this.name = jsonObject.getString("name");
        this.password = jsonObject.getString("password");
        this.role = jsonObject.getString("role");
    }
}
