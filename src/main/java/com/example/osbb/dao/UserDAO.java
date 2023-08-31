package com.example.osbb.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.osbb.entity.authorization.User;

@EnableJpaRepositories
public interface UserDAO extends JpaRepository<User, Long> {

   User findByUsername(String username);

   boolean existsById(long id);

   boolean existsByUsername(String username);

}
