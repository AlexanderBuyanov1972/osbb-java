package com.example.osbb.controller;

import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.service.ServicePojo;
import com.example.osbb.service.registry.IRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstants.REGISTRY)
public class RegistryController {
    @Autowired
    IRegistryService iRegistryService;

    @Autowired
    ServicePojo servicePojo;
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

    //----------------------------------------------------------------------
    // check
    @GetMapping(ApiConstants.APARTMENT + ApiConstants.PARAM_4)
    Object getPojoRoomAndListClients(@PathVariable String apartment) {
        return servicePojo.getPojoRoomAndListClients(apartment);
    }

    //check
    @GetMapping(ApiConstants.FULLNAME + ApiConstants.ALL + ApiConstants.PARAM_3)
    Object getListQuestionnaireByFullName(@PathVariable String fullname) {
        return servicePojo.getListQuestionnaireByFullName(fullname);
    }

    //check
    @GetMapping(ApiConstants.FULLNAME + ApiConstants.PARAM_3)
    Object getPojoClientAndListRooms(@PathVariable String fullname) {
        return servicePojo.getPojoClientAndListRooms(fullname);
    }

    //check
    @GetMapping(ApiConstants.SHARE + ApiConstants.FULLNAME + ApiConstants.PARAM_3)
    Object getShareAreaFromHouseByFIO(@PathVariable String fullname) {
        return servicePojo.getShareAreaFromHouseByFIO(fullname);
    }

    //check
    @GetMapping(ApiConstants.SHARE + ApiConstants.TOTAL_AREA)
    Object getListClientAndTotalArea() {
        return servicePojo.getListClientAndTotalArea();
    }

}
