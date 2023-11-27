package com.example.osbb.dao;

import com.example.osbb.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface RecordDAO extends JpaRepository<Record, Long> {
    Record findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(String apartment,
                                                                                              String lastName,
                                                                                              String firstName,
                                                                                              String secondName);

    boolean existsByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(String apartment,
                                                                                                 String lastName,
                                                                                                 String firstName,
                                                                                                 String secondName);

    boolean existsByOwnershipIdAndOwnerId(long OwnershipId, long OwnerId);

    List<Record> findByOwnershipId(Long id);

    List<Record> findByOwnerId(Long id);
}



