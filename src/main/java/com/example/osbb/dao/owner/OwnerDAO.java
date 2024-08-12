package com.example.osbb.dao.owner;

import com.example.osbb.entity.owner.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;

@EnableJpaRepositories
public interface OwnerDAO extends JpaRepository<Owner, Long> {

    Owner findByLastNameAndFirstNameAndSecondName(
            String lastName,
            String firstName,
            String secondName);


    boolean existsByLastNameAndFirstNameAndSecondNameAndDateBirth(
            String lastName,
            String firstName,
            String secondName,
            LocalDate dateBirth);

}
