package com.example.osbb.security.service.cookie;

import jakarta.servlet.http.Cookie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class CookieService implements ICookieService {

    @Value("#{T(Integer).parseInt('${age.max.cookie}')}")
    private int ageMaxCookie;

    @Override
    public Cookie addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(ageMaxCookie);
        log.info("cookie : {} ", cookie.toString());
        return cookie;
    }

    @Override
    public Cookie getCookie(String name, HttpServletRequest request) {
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
        log.info("cookie : {} ", cookie.toString());
        return cookie;
    }

}
