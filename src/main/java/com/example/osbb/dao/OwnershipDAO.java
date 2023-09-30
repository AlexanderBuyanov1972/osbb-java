package com.example.osbb.dao;

import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnershipDAO extends JpaRepository<Ownership, Long> {
    long countByTypeRoom(TypeOfRoom typeRoom);
    List<Ownership> findByAddressApartment(String apartment);
    long count();

}

