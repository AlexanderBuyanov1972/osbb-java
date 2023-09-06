package com.example.osbb.dao;

import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnershipDAO extends JpaRepository<Ownership, Long> {
    long countByTypeRoom(TypeOfRoom typeRoom);
    long count();

}
