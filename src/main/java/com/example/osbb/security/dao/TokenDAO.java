package com.example.osbb.security.dao;

import com.example.osbb.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TokenDAO extends JpaRepository<RefreshToken, Long> {

    RefreshToken findTokenByUsername(String username);

    void removeByUsername(String username);
}
