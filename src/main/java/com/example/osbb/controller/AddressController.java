package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.ownership.Address;
import com.example.osbb.service.address.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.ADDRESS)
public class AddressController {

    @Autowired
    private IAddressService service;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody Address address) {
        return service.createAddress(address);
    }

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody Address address) {
        return service.updateAddress(address);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getAddress(@PathVariable Long id) {
        return service.getAddress(id);
    }

    @GetMapping()
    public ResponseEntity<?> getAddressStart() {
        return service.getAddressStart();
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        return service.deleteAddress(id);
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllAddress(@RequestBody List<Address> list) {
        return service.createAllAddress(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllAddress(@RequestBody List<Address> list) {
        return service.updateAllAddress(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllAddress() {
        return service.getAllAddress();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllAddress() {
        return service.deleteAllAddress();
    }

    // ----------- street, house and number apartment ------------

    @GetMapping(ApiPaths.PARAM_STREET + ApiPaths.PARAM_HOUSE + ApiPaths.APARTMENT)
    public ResponseEntity<?> getAddress(
            @PathVariable String street,
            @PathVariable String house,
            @PathVariable String apartment) {
        return service.getAddress(street, house, apartment);
    }


}
