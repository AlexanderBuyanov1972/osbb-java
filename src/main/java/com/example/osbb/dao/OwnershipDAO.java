package com.example.osbb.dao;

import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface OwnershipDAO extends JpaRepository<Ownership, Long> {
    boolean existsByAddressApartment(String apartment);

    boolean existsByBill(String bill);

    List<Ownership> findByAddressApartment(String apartment);

    Ownership findByBill(String bill);

    long countByTypeRoom(TypeOfRoom typeRoom);

    long count();


}

