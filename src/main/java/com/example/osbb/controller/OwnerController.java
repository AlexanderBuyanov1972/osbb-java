package com.example.osbb.controller;

import com.example.osbb.entity.Owner;
import com.example.osbb.service.owner.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.OWNER)
public class OwnerController {

    @Autowired
    private IOwnerService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createOwner(@RequestBody Owner owner) {
        return response.returnResponse(service.createOwner(owner));
    }

    @PutMapping
    public ResponseEntity<?> updateOwner(@RequestBody Owner owner) {
        return response.returnResponse(service.updateOwner(owner));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getOwner(@PathVariable Long id) {
        return response.returnResponse(service.getOwner(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteOwner(@PathVariable Long id) {
        return response.returnResponse(service.deleteOwner(id));
    }

    // -------------- all ----------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllOwner(@RequestBody List<Owner> list) {
        return response.returnResponse(service.createAllOwner(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllOwner(@RequestBody List<Owner> list) {
        return response.returnResponse(service.updateAllOwner(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllOwner() {
        return response.returnResponse(service.getAllOwner());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllOwner() {
        return response.returnResponse(service.deleteAllOwner());
    }

    // ----------- count ---------------
    @GetMapping(ApiConstants.COUNT)
    public ResponseEntity<?> countOwners() {
        return response.returnResponse(service.countOwners());
    }

    // get owner by full name
    @GetMapping(ApiConstants.FULLNAME +  ApiConstants.PARAM_3)
    public ResponseEntity<?> getOwnerByFullName(@PathVariable String fullname) {
        return response.returnResponse(service.getOwnerByFullName(fullname));
    }




}
