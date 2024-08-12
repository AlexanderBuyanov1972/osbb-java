package com.example.osbb.controller.owner;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.service.owner.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.OWNER)
public class OwnerController {

    @Autowired
    private IOwnerService service;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createOwner(@RequestBody Owner owner) {
        return service.createOwner(owner);
    }

    @PutMapping
    public ResponseEntity<?> updateOwner(@RequestBody Owner owner) {
        return service.updateOwner(owner);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getOwner(@PathVariable Long id) {
        return service.getOwner(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deleteOwner(@PathVariable Long id) {
        return service.deleteOwner(id);
    }

    // -------------- all ----------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllOwner(@RequestBody List<Owner> list) {
        return service.createAllOwner(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllOwner(@RequestBody List<Owner> list) {
        return service.updateAllOwner(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllOwner() {
        return service.getAllOwner();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllOwner() {
        return service.deleteAllOwner();
    }

    // ----------- count ---------------
    @GetMapping(ApiPaths.COUNT)
    public ResponseEntity<?> countOwners() {
        return service.countOwners();
    }

    // get owner by full name
    @GetMapping(ApiPaths.FULL_NAME + ApiPaths.PARAM_FULL_NAME)
    public ResponseEntity<?> getOwnerByFullName(@PathVariable String fullName) {
        return service.getOwnerByFullName(fullName);
    }


}
