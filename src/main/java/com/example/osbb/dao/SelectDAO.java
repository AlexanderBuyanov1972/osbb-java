package com.example.osbb.dao;

;
import com.example.osbb.entity.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface SelectDAO extends JpaRepository<Select, Long> {

}
