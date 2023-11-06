package com.example.osbb.dao;

import com.example.osbb.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TokenDAO extends JpaRepository<RefreshToken, Long> {
    RefreshToken findTokenByEmail(String email);
    void removeByEmail(String email);
}
