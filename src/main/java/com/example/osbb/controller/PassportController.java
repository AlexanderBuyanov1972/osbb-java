package com.example.osbb.controller;

import com.example.osbb.consts.ApiConstants;
import com.example.osbb.entity.Passport;
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

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getPassword(@PathVariable Long id) {
        return response.returnResponse(service.getPassport(id));
    }

    @DeleteMapping(ApiConstants.ID)
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

    @GetMapping(ApiConstants.REGISTRATION_NUMBER + ApiConstants.REGISTRATION_NUMBER_CARD_PAYER_TAXES)
    public ResponseEntity<?> getByRegistrationNumberCardPayerTaxes(@PathVariable String number) {
        return response.returnResponse(service.findByRegistrationNumberCardPayerTaxes(number));
    }

}
