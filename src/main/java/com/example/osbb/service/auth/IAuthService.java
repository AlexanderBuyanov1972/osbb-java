package com.example.osbb.service.auth;

import com.example.osbb.dto.Auth;

public interface IAuthService {

    public Object login(Auth auth);

    public Object logout();

}
