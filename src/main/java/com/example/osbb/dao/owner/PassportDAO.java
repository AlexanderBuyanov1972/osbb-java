package com.example.osbb.dao.owner;

import com.example.osbb.entity.owner.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PassportDAO extends JpaRepository<Passport, Long> {
    boolean existsById(long id);
    Passport findByRegistrationNumberCardPayerTaxes(String registrationNumberCardPayerTaxes);
}
