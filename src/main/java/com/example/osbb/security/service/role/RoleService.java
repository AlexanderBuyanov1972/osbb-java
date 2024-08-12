package com.example.osbb.security.service.role;

import com.example.osbb.security.dao.RoleDAO;
import com.example.osbb.security.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService implements IRoleService {
    @Autowired
    RoleDAO roleDAO;

    @Override
    public Role createRole(String name) {
        return roleDAO.existsByName(name) ? null : roleDAO.save(new Role(name));
    }

    @Override
    public Role getRole(Integer id) {
        return roleDAO.findById(id).orElse(null);
    }
}
