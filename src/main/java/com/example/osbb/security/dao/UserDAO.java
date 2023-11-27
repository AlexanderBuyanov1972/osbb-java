package com.example.osbb.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.osbb.security.entity.User;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface UserDAO extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByActivationLink(String activationLink);

    User findByEmail(String email);

    User findByUsername(String username);

    User findByActivationLink(String activationLink);

}
