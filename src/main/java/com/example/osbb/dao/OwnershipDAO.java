package com.example.osbb.dao;

import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface OwnershipDAO extends JpaRepository<Ownership, Long> {
    boolean existsByAddressApartment(String apartment);

    Ownership findByAddressApartment(String apartment);

    long countByTypeRoom(TypeOfRoom typeRoom);

    long count();

}

