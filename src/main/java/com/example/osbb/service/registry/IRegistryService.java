package com.example.osbb.service.registry;

import org.springframework.http.ResponseEntity;

public interface IRegistryService {
    ResponseEntity<?> getRegistryOwners();

    ResponseEntity<?> getRegistryOwnerships();

    ResponseEntity<?> getBuildingCharacteristics();

}
