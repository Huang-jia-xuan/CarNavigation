package edu.jnu.component;

import edu.jnu.entity.UserCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CookieExpirationCleaner {

    @Scheduled(fixedRate = 60000)  // 每隔1分钟检查一次
    public void cleanExpiredCookies() {
        long currentTime = System.currentTimeMillis();
        // 遍历Map，删除过期的cookie
        UserCache.getAllCookies().entrySet().removeIf(entry -> {long livingTime = currentTime-entry.getValue().getCreateTime();return livingTime>360000;});
        System.out.println("Expired cookies cleaned.");
    }
}
