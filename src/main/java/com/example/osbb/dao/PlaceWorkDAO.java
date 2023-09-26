package com.example.osbb.dao;

import com.example.osbb.entity.PlaceWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;

@EnableJpaRepositories
public interface PlaceWorkDAO extends JpaRepository<PlaceWork, Long> {

}
