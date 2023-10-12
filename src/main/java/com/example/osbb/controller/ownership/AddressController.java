package com.example.osbb.controller.ownership;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.entity.ownership.Address;
import com.example.osbb.service.address.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.ADDRESS)
public class AddressController {

    @Autowired
    private IAddressService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody Address address) {
        return response.returnResponse(service.createAddress(address));
    }

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody Address address) {
        return response.returnResponse(service.updateAddress(address));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getAddress(@PathVariable Long id) {
        return response.returnResponse(service.getAddress(id));
    }
    @GetMapping()
    public ResponseEntity<?> getAddressStart() {
        return response.returnResponse(service.getAddressStart());
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        return response.returnResponse(service.deleteAddress(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllAddress(@RequestBody List<Address> list) {
        return response.returnResponse(service.createAllAddress(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllAddress(@RequestBody List<Address> list) {
        return response.returnResponse(service.updateAllAddress(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllAddress() {
        return response.returnResponse(service.getAllAddress());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllAddress() {
        return response.returnResponse(service.deleteAllAddress());
    }

    // ----------- street, house and number apartment ------------

    @GetMapping(ApiConstants.PARAM_STREET + ApiConstants.PARAM_HOUSE + ApiConstants.APARTMENT)
    public ResponseEntity<?> getAddress(
            @PathVariable String street,
            @PathVariable String house,
            @PathVariable String apartment) {
        return response.returnResponse(service.getAddress(street, house, apartment));
    }


}
