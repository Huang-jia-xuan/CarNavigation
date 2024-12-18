package edu.jnu.controller;

import com.alibaba.fastjson2.JSONObject;
import edu.jnu.entity.AuthDTO;
import edu.jnu.entity.RegisterVO;
import edu.jnu.entity.UserDTO;
import edu.jnu.service.RegisterService;
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
 * @创建日期: 2023年05月01日 18时35分
 * @功能描述: 用户注册接口
 */
@Slf4j
@RestController
@CrossOrigin
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String jsonString) {

        System.out.println(jsonString);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        RegisterVO registerVO = new RegisterVO(jsonObject);
        // 将用户数据插入数据库
        UserDTO userDTO = UserDTO.builder()
                .name(registerVO.getName())
                .role(registerVO.getRole())
                .build();
        if(registerService.isExist(userDTO)){
            return ResponseEntity.badRequest().body("用户已存在");
        }
        // 将用户名和密码写入身份认证表
        AuthDTO authDTO = AuthDTO.builder()
                .userName(registerVO.getName())
                .password(registerVO.getPassword())
                .build();
        if (registerService.add(authDTO)) {
            return ResponseEntity.ok("注册成功");
        } else {
            return ResponseEntity.badRequest().body("注册失败，可能是网络抖动");
        }
    }
}
