package com.example.osbb.service.passport;

import com.example.osbb.entity.owner.Passport;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPassportService {

    // ------------------- one -----------------------
    ResponseEntity<?> createPassport(Passport passport);

    ResponseEntity<?> updatePassport(Passport passport);

    ResponseEntity<?> getPassport(Long id);

    ResponseEntity<?> deletePassport(Long id);

    // ------------------ all ----------------

    ResponseEntity<?> createAllPassport(List<Passport> list);

    ResponseEntity<?> updateAllPassport(List<Passport> list);

    ResponseEntity<?> getAllPassport();

    ResponseEntity<?> deleteAllPassport();

    // ------------- street, house and apartment ----------------

    ResponseEntity<?> findByRegistrationNumberCardPayerTaxes(String number);


}
