package com.example.osbb.dao.owner;

import com.example.osbb.entity.owner.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;

@EnableJpaRepositories
public interface OwnerDAO extends JpaRepository<Owner, Long> {

    long count();

    Owner findByLastNameAndFirstNameAndSecondName(
            String lastName,
            String firstName,
            String secondName);

    boolean existsByLastNameAndFirstNameAndSecondName(
            String lastName,
            String firstName,
            String secondName);

    Owner findByLastNameAndFirstNameAndSecondNameAndDateBirth(
            String lastName,
            String firstName,
            String secondName,
            LocalDate dateBirth);

    boolean existsByLastNameAndFirstNameAndSecondNameAndDateBirth(
            String lastName,
            String firstName,
            String secondName,
            LocalDate dateBirth);

}
