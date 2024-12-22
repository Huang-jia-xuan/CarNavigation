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
    public ResponseEntity<String> login(@RequestBody String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        System.out.println(jsonString);
        BasicOperation basicOperation = new BasicOperation();
        int res = basicOperation.login(jsonObject.getInteger("id"), jsonObject.getString("password"));
        System.out.println(res);
        if (res == 1) {
            return ResponseEntity.ok("密码错误");
        }
        if (res == 2) {
            return ResponseEntity.ok("登陆成功");

        }
        return ResponseEntity.ok("用户不存在");
    }
}
