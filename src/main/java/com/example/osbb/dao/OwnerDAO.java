package com.example.osbb.dao;

import com.example.osbb.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface OwnerDAO extends JpaRepository<Owner, Long> {

   boolean existsById(long id);

}
