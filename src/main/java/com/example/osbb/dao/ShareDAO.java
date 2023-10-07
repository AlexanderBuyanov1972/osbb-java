package com.example.osbb.dao;

import com.example.osbb.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ShareDAO extends JpaRepository<Share, Long> {
    Share findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(String apartment,
                                                                                             String lastName,
                                                                                             String firstName,
                                                                                             String secondName);

    boolean existsByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(String apartment,
                                                                                                 String lastName,
                                                                                                 String firstName,
                                                                                                 String secondName);


    Share findByOwnerIdAndOwnershipId(long ownerId, long ownershipId);
}
