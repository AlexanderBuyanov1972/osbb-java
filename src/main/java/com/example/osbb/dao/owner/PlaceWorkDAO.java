package com.example.osbb.dao.owner;

import com.example.osbb.entity.owner.PlaceWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PlaceWorkDAO extends JpaRepository<PlaceWork, Long> {

}
