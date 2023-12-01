package com.example.osbb.service;

import jakarta.servlet.http.Cookie;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class CookieService {
    private static final Logger log = Logger.getLogger(CookieService.class);
    @Value("#{T(Integer).parseInt('${age.max.cookie}')}")
    private int ageMaxCookie;

    public Cookie addCookie(String name, String value) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(messageEnter(methodName)));
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(ageMaxCookie);
        log.info("cookie : " + cookie.toString());
        log.info(messageExit(methodName));
        return cookie;
    }

    public Cookie getCookie(String name, HttpServletRequest request) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cook : cookies) {
                if (name.equals(cook.getName())) {
                    cookie = cook;
                    break;
                }
            }
        }
        assert cookie != null;
        log.info("cookie : " + cookie.toString());
        log.info(messageExit(methodName));
        return cookie;
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
