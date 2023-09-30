package com.example.osbb.dao;

import com.example.osbb.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface RecordDAO extends JpaRepository<Record, Long> {


}
