package com.example.osbb.dao;

import com.example.osbb.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PasswordDAO extends JpaRepository<Password, Long> {
    boolean existsById(long id);
    boolean existsByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes);
    Password findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes);
}
