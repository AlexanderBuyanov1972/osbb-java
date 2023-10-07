package com.example.osbb.dao;

import com.example.osbb.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface AccountDAO extends JpaRepository<Account, Long> {

}
