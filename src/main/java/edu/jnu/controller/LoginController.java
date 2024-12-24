package edu.jnu.controller;

import com.alibaba.fastjson2.JSONObject;
import edu.jnu.Operation.BasicOperation;
import edu.jnu.entity.LoginVO;
import edu.jnu.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月01日 20时56分
 * @功能描述: 用户登陆接口
 */
@RestController
@Slf4j
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<JSONObject> login(@RequestBody String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        BasicOperation basicOperation = new BasicOperation();
        String res = basicOperation.login(jsonObject.getInteger("id"), jsonObject.getString("password"));

        JSONObject response = new JSONObject();

        if ("passwordError".equals(res)) {
            response.put("success", false);
            response.put("message", "密码错误");
            return ResponseEntity.ok(response);
        }
        if ("userNotFound".equals(res)) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return ResponseEntity.ok(response);
        }

        // 登录成功，返回用户身份信息（'admin' 或 'user'）
        response.put("success", true);
        response.put("message", "登录成功");
        response.put("userType", res); // 添加用户类型（admin 或 user）
        return ResponseEntity.ok(response);
    }
}
