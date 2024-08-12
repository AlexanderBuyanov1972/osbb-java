package com.example.osbb.service.address;

import com.example.osbb.entity.ownership.Address;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAddressService {

    // ------------------- one -----------------------
    ResponseEntity<?> createAddress(Address address);

    ResponseEntity<?> updateAddress(Address address);

    ResponseEntity<?> getAddress(Long id);

    ResponseEntity<?> getAddressStart();

    ResponseEntity<?> deleteAddress(Long id);

    // ------------------ all ----------------

    ResponseEntity<?> createAllAddress(List<Address> list);

    ResponseEntity<?> updateAllAddress(List<Address> list);

    ResponseEntity<?> getAllAddress();

    ResponseEntity<?> deleteAllAddress();

    //------------- street, house and apartment ----------------

    ResponseEntity<?> getAddress(String street, String house, String apartment);

}
