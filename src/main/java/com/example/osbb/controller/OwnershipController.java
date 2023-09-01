package com.example.osbb.controller;

import com.example.osbb.consts.ApiConstants;
import com.example.osbb.entity.Ownership;
import com.example.osbb.service.ownership.IOwnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.OWNERSHIP)
public class OwnershipController {

    @Autowired
    private IOwnershipService service;

    @Autowired
    private HelpMethodsForController response;

    // ---------- one -------------------

    @PostMapping
    public ResponseEntity<?> createObjectOwnership(@RequestBody Ownership one) {
        return response.returnResponse(service.createOwnership(one));
    }

    @PutMapping
    public ResponseEntity<?> updateObjectOwnership(@RequestBody Ownership one) {
        return response.returnResponse(service.updateOwnership(one));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getObjectOwnership(@PathVariable Long id) {
        return response.returnResponse(service.getOwnership(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteOwnership(@PathVariable Long id) {
        return response.returnResponse(service.deleteOwnership(id));
    }

    // ------------------- all ----------------
    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllObjectOwnership(@RequestBody List<Ownership> list) {
        return response.returnResponse(service.createAllOwnership(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllObjectOwnership(@RequestBody List<Ownership> list) {
        return response.returnResponse(service.updateAllOwnership(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllObjectOwnership() {
        return response.returnResponse(service.getAllOwnership());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllObjectOwnership() {
        return response.returnResponse(service.deleteAllOwnership());
    }

    // ------------- summa areas ------------

    // Общая площадь жилых и нежилых помещений
    @GetMapping(ApiConstants.SUMMA_AREA_ROOMS)
    public ResponseEntity<?> summaAreaRooms() {
        return response.returnResponse(service.summaAreaRooms());
    }

    // Площадь квартир
    @GetMapping(ApiConstants.SUMMA_AREA_APARTMENT)
    public ResponseEntity<?> summaAreaApartment() {
        return response.returnResponse(service.summaAreaApartment());
    }

    // Площадь нежилых помещений
    @GetMapping(ApiConstants.SUMMA_AREA_NON_RESIDENTIAL_ROOM)
    public ResponseEntity<?> summaAreaNonResidentialRoom() {
        return response.returnResponse(service.summaAreaNonResidentialRoom());
    }

    // ------------- count rooms -----------------

    // Количество квартир
    @GetMapping(ApiConstants.COUNT_ROOMS)
    public ResponseEntity<?> countRooms() {
        return response.returnResponse(service.countRooms());
    }

    @GetMapping(ApiConstants.COUNT_APARTMENT)
    public ResponseEntity<?> countApartment() {
        return response.returnResponse(service.countApartment());
    }

    // Количество нежилых помещений
    @GetMapping(ApiConstants.COUNT_NON_RESIDENTIAL_ROOM)
    public ResponseEntity<?> countNonResidentialRoom() {
        return response.returnResponse(service.countNonResidentialRoom());
    }
    // -------------- specific-------------------
    @GetMapping(ApiConstants.ONE_OWNERSHIP_LIST_OWNER + ApiConstants.ID)
    public ResponseEntity<?> getOwnershipWithListOwner(@PathVariable Long id){
        return response.returnResponse(service.getOwnershipWithListOwner(id));
    }


}
