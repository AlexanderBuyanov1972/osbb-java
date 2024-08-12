package com.example.osbb.security.service.role;

import com.example.osbb.security.entity.Role;

public interface IRoleService {

    Role createRole(String name);

    Role getRole(Integer id);

}
