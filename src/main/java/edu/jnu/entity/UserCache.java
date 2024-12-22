package edu.jnu.entity;

import edu.jnu.entity.Cookie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserCache {
    private static final Map<Integer,Cookie> cookieStorage = new ConcurrentHashMap<>();
    public static void addCookie(int userId, Cookie userCookie) {
        cookieStorage.put(userId, userCookie);
    }

    public Cookie getCookie(int userId) {
        return cookieStorage.get(userId);
    }

    public static void removeCookie(int userId) {
        cookieStorage.remove(userId);
    }

    public static Map<Integer,Cookie> getAllCookies() {
        return cookieStorage;
    }
}
