package com.example.osbb.dao;

import com.example.osbb.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;

@EnableJpaRepositories
public interface RateDAO extends JpaRepository<Rate, Long> {
    boolean existsByDate(LocalDate date);
    Rate findByDate(LocalDate date);
}
