package com.example.osbb.dao;

import com.example.osbb.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface OwnerDAO extends JpaRepository<Owner, Long> {
    boolean existsById(long id);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    long count();
    Owner findByLastNameAndFirstNameAndSecondName(String lastname, String firstName, String secondName);

}
