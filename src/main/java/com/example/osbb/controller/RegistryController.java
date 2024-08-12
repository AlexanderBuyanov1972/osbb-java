package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.service.registry.IRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.REGISTRY)
public class RegistryController {
    @Autowired
    IRegistryService service;

    @Autowired
    OwnershipDAO ownershipDAO;

    @GetMapping(ApiPaths.OWNER)
    ResponseEntity<?> getRegistryOwners() {
        return service.getRegistryOwners();
    }

    @GetMapping(ApiPaths.OWNERSHIP)
    ResponseEntity<?> getRegistryOwnerships() {
        return service.getRegistryOwnerships();
    }

    @GetMapping(ApiPaths.CHARACTERISTICS)
    ResponseEntity<?> getBuildingCharacteristics() {
        return service.getBuildingCharacteristics();
    }

}
