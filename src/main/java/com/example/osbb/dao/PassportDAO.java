package com.example.osbb.dao;

import com.example.osbb.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PassportDAO extends JpaRepository<Passport, Long> {
    boolean existsById(long id);
    boolean existsByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes);
    Passport findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes);
}
