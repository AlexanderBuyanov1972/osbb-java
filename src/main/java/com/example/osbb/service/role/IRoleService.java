package com.example.osbb.service.role;

import com.example.osbb.entity.authorization.Role;

import java.util.List;
import java.util.Set;

public interface IRoleService {

    // ----- one -----
    public Object createRole(Role role);

    public Object updateRole(Role role);

    public Object getRole(Long id);

    public Object deleteRole(Long id);

    // ----- all -----

    public Object createAllRole(List<Role> list);

    public Object updateAllRole(List<Role> list);

    public Object getAllRole();

    public Object deleteAllRole();


}
