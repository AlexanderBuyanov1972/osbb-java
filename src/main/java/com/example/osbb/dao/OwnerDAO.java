package com.example.osbb.dao;

import com.example.osbb.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;

@EnableJpaRepositories
public interface OwnerDAO extends JpaRepository<Owner, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    long count();

    Owner findByLastNameAndFirstNameAndSecondName(String lastName, String firstName, String secondName);

    Owner findByLastNameAndFirstNameAndSecondNameAndDateBirth(String lastName, String firstName, String secondName, LocalDate dateBirth);

    boolean existsByLastNameAndFirstNameAndSecondName(String lastname, String firstName, String secondName);

    boolean existsByLastNameAndFirstNameAndSecondNameAndDateBirth(String lastname, String firstName, String secondName, LocalDate dateBirth);

}
