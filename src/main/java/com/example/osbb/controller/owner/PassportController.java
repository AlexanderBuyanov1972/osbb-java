package com.example.osbb.controller.owner;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.entity.owner.Passport;
import com.example.osbb.service.passport.IPassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PASSPORT)
public class PassportController {

    @Autowired
    private IPassportService service;

    @Autowired
    private HelpMethodsForController response;

    // ------------------- one -------------------

    @PostMapping
    public ResponseEntity<?> createPassport(@RequestBody Passport passport) {
        return response.returnResponse(service.createPassport(passport));
    }

    @PutMapping
    public ResponseEntity<?> updatePassword(@RequestBody Passport password) {
        return response.returnResponse(service.updatePassport(password));
    }

    @GetMapping(ApiConstants.PARAM_ID)
    public ResponseEntity<?> getPassword(@PathVariable Long id) {
        return response.returnResponse(service.getPassport(id));
    }

    @DeleteMapping(ApiConstants.PARAM_ID)
    public ResponseEntity<?> deletePassword(@PathVariable Long id) {
        return response.returnResponse(service.deletePassport(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllPassword(@RequestBody List<Passport> list) {
        return response.returnResponse(service.createAllPassport(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllPassword(@RequestBody List<Passport> list) {
        return response.returnResponse(service.updateAllPassport(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllPassword() {
        return response.returnResponse(service.getAllPassport());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllPassword() {
        return response.returnResponse(service.deleteAllPassport());
    }

    // ----------- street, house and number apartment ------------

    @GetMapping(ApiConstants.REGISTRATION_NUMBER + ApiConstants.PARAM_ID)
    public ResponseEntity<?> getByRegistrationNumberCardPayerTaxes(@PathVariable String number) {
        return response.returnResponse(service.findByRegistrationNumberCardPayerTaxes(number));
    }

}
