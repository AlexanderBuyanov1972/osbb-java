package com.example.osbb.security.service.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface ICookieService {

    Cookie addCookie(String name, String value);

    Cookie getCookie(String name, HttpServletRequest request);
}
