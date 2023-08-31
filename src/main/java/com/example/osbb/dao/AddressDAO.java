package com.example.osbb.dao;

import com.example.osbb.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface AddressDAO extends JpaRepository<Address, Long> {
    boolean existsById(long id);

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
