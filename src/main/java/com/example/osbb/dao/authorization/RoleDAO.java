package com.example.osbb.dao.authorization;

import com.example.osbb.entity.authorization.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface RoleDAO extends JpaRepository<Role, Long> {
    boolean existsById(long id);
    boolean existsByName(String name);
    Role findByName(String name);

}