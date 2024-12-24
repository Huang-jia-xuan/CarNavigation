package edu.jnu.service;

import edu.jnu.Operation.BasicOperation;
import edu.jnu.dao.UserAuthDao;
import edu.jnu.entity.Cookie;
import edu.jnu.entity.UserAuth;
import edu.jnu.entity.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月01日 20时59分
 * @功能描述: 登陆功能业务类
 */
@Service
@Slf4j
public class LoginService {
    private long maintainTime = 360000;
    @Autowired
    private UserAuthDao userAuthDao;

//    @Autowired
//    private UserCache userCache;
    BasicOperation basicOperation;
    public Cookie checkPassword(int userId, String password) {
        if(basicOperation==null){
            basicOperation = new BasicOperation();
        }
        int res = basicOperation.login(userId, password);
        if(res==2){
            String cookie = UUID.randomUUID().toString();
            long createTime = System.currentTimeMillis();
            Cookie userCookie = new Cookie(cookie,createTime);
            UserCache.addCookie(userId,userCookie);
            return userCookie;
        }
        return null;
    }
}
