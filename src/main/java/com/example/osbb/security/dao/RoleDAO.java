package com.example.osbb.security.dao;

import com.example.osbb.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface RoleDAO extends JpaRepository<Role, Integer> {

    boolean existsByName(String name);
}
