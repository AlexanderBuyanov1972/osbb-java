package com.example.osbb.dao.ownership;

import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface OwnershipDAO extends JpaRepository<Ownership, Long> {
    boolean existsByAddressApartment(String apartment);

    Ownership findByAddressApartment(String apartment);

    long countByTypeRoom(TypeOfRoom typeRoom);

    long count();

}

