package com.example.osbb.controller;

import com.example.osbb.consts.ApiConstants;
import com.example.osbb.entity.Password;
import com.example.osbb.service.password.IPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PASSWORD)
public class PasswordController {

    @Autowired
    private IPasswordService service;

    @Autowired
    private HelpMethodsForController response;

    // ------------------- one -------------------

    @PostMapping
    public ResponseEntity<?> createPassword(@RequestBody Password password) {
        return response.returnResponse(service.createPassword(password));
    }

    @PutMapping
    public ResponseEntity<?> updatePassword(@RequestBody Password password) {
        return response.returnResponse(service.updatePassword(password));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getPassword(@PathVariable Long id) {
        return response.returnResponse(service.getPassword(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deletePassword(@PathVariable Long id) {
        return response.returnResponse(service.deletePassword(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllPassword(@RequestBody List<Password> list) {
        return response.returnResponse(service.createAllPassword(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllPassword(@RequestBody List<Password> list) {
        return response.returnResponse(service.updateAllPassword(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllPassword() {
        return response.returnResponse(service.getAllPassword());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllPassword() {
        return response.returnResponse(service.deleteAllPassword());
    }

    // ----------- street, house and number apartment ------------

    @GetMapping(ApiConstants.REGISTRATION_NUMBER + ApiConstants.REGISTRATION_NUMBER_CARD_PAYER_TAXES)
    public ResponseEntity<?> getByRegistrationNumberCardPayerTaxes(@PathVariable String number) {
        return response.returnResponse(service.findByRegistrationNumberCardPayerTaxes(number));
    }

}
