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
    public ResponseEntity<JSONObject> register(@RequestBody String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        RegisterVO registerVO = new RegisterVO(jsonObject);

        // 检查必填字段
        if (registerVO.getId() == 0 || registerVO.getName() == null || registerVO.getPassword() == null) {
            JSONObject response = new JSONObject();
            response.put("message", "缺少必要的字段");
            return ResponseEntity.badRequest().body(response);
        }

        // 调用注册服务
        AuthDTO authDTO = AuthDTO.builder()
                .userId(registerVO.getId())
                .userName(registerVO.getName())
                .password(registerVO.getPassword())
                .code(registerVO.getRole())
                .build();

        boolean isRegistered = registerService.add(authDTO);

        JSONObject response = new JSONObject();
        if (isRegistered) {
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);
        } else {
            // 如果注册失败，返回用户已存在的错误信息
            response.put("message", "用户ID " + registerVO.getId() + " 已经存在！");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
