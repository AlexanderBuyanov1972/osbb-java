package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.service.registry.IRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.REGISTRY)
public class RegistryController {
    @Autowired
    IRegistryService iRegistryService;

    @Autowired
    OwnershipDAO ownershipDAO;

    @Autowired
    HelpMethodsForController response;

    @GetMapping(ApiConstants.OWNER)
    ResponseEntity<?> getRegistryOwners() {
        return response.returnResponse(iRegistryService.getRegistryOwners());
    }

    @GetMapping(ApiConstants.OWNERSHIP)
    ResponseEntity<?> getRegistryOwnerships() {
        return response.returnResponse(iRegistryService.getRegistryOwnerships());
    }

    @GetMapping(ApiConstants.CHARACTERISTICS)
    ResponseEntity<?> getBuildingCharacteristics() {
        return response.returnResponse(iRegistryService.getBuildingCharacteristics());
    }

}
