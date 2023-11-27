package com.example.osbb.dao;

import com.example.osbb.entity.ownership.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface AddressDAO extends JpaRepository<Address, Long> {
    boolean existsByApartment(String apartment);
    Address findByApartment(String apartment);

    boolean existsByStreetAndHouseAndApartment(
            String street,
            String house,
            String apartment
    );

    Address findByStreetAndHouseAndApartment(
            String street,
            String house,
            String apartment
    );
}
