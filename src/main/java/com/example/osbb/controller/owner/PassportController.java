package com.example.osbb.controller.owner;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.owner.Passport;
import com.example.osbb.service.passport.IPassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.PASSPORT)
public class PassportController {

    @Autowired
    private IPassportService service;

    // ------------------- one -------------------

    @PostMapping
    public ResponseEntity<?> createPassport(@RequestBody Passport passport) {
        return service.createPassport(passport);
    }

    @PutMapping
    public ResponseEntity<?> updatePassword(@RequestBody Passport password) {
        return service.updatePassport(password);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getPassword(@PathVariable Long id) {
        return service.getPassport(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deletePassword(@PathVariable Long id) {
        return service.deletePassport(id);
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllPassword(@RequestBody List<Passport> list) {
        return service.createAllPassport(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllPassword(@RequestBody List<Passport> list) {
        return service.updateAllPassport(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllPassword() {
        return service.getAllPassport();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllPassword() {
        return service.deleteAllPassport();
    }

    // ----------- street, house and number apartment ------------

    @GetMapping(ApiPaths.REGISTRATION_NUMBER + ApiPaths.PARAM_ID)
    public ResponseEntity<?> getByRegistrationNumberCardPayerTaxes(@PathVariable String number) {
        return service.findByRegistrationNumberCardPayerTaxes(number);
    }

}
