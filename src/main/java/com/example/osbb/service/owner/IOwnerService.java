package com.example.osbb.service.owner;

import com.example.osbb.entity.owner.Owner;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOwnerService {

    // one
    ResponseEntity<?> createOwner(Owner owner);

    ResponseEntity<?> updateOwner(Owner owner);


    ResponseEntity<?> getOwner(Long id);

    ResponseEntity<?> deleteOwner(Long id);

    // all

    ResponseEntity<?> createAllOwner(List<Owner> owners);

    ResponseEntity<?> updateAllOwner(List<Owner> owners);

    ResponseEntity<?> getAllOwner();

    ResponseEntity<?> deleteAllOwner();

    // count
    ResponseEntity<?> countOwners();

    // get owner by full name
    ResponseEntity<?> getOwnerByFullName(String fullName);


}
