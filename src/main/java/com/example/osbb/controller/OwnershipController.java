package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.service.ownership.IOwnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.OWNERSHIP)
public class OwnershipController {

    @Autowired
    private IOwnershipService service;

    // ---------- one -------------------

    @PostMapping
    public ResponseEntity<?> createOwnership(@RequestBody Ownership one) {
        return service.createOwnership(one);
    }

    @PutMapping
    public ResponseEntity<?> updateOwnership(@RequestBody Ownership one) {
        return service.updateOwnership(one);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getOwnership(@PathVariable Long id) {
        return service.getOwnership(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deleteOwnership(@PathVariable Long id) {
        return service.deleteOwnership(id);
    }

    // ------------------- all ----------------
    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllOwnership(@RequestBody List<Ownership> list) {
        return service.createAllOwnership(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllOwnership(@RequestBody List<Ownership> list) {
        return service.updateAllOwnership(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllOwnership() {
        return service.getAllOwnership();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllOwnership() {
        return service.deleteAllOwnership();
    }

    // ------------- summa areas ------------

    // Общая площадь жилых и нежилых помещений
    @GetMapping(ApiPaths.SUMMA_AREA_ROOMS)
    public ResponseEntity<?> summaAreaRooms() {
        return service.summaAreaRooms();
    }

    // Площадь квартир
    @GetMapping(ApiPaths.SUMMA_AREA_APARTMENT)
    public ResponseEntity<?> summaAreaApartment() {
        return service.summaAreaApartment();
    }

    //  Жилая площадь квартир
    @GetMapping(ApiPaths.SUMMA_AREA_LIVING_APARTMENT)
    public ResponseEntity<?> summaAreaLivingApartment() {
        return service.summaAreaLivingApartment();
    }

    // Площадь нежилых помещений
    @GetMapping(ApiPaths.SUMMA_AREA_NON_RESIDENTIAL_ROOM)
    public ResponseEntity<?> summaAreaNonResidentialRoom() {
        return service.summaAreaNonResidentialRoom();
    }

    // ------------- count rooms -----------------

    // Количество помещений
    @GetMapping(ApiPaths.COUNT_ROOMS)
    public ResponseEntity<?> countRooms() {
        return service.countRooms();
    }

    // Количество квартир
    @GetMapping(ApiPaths.COUNT_APARTMENT)
    public ResponseEntity<?> countApartment() {
        return service.countApartment();
    }

    // Количество нежилых помещений -----------------------
    @GetMapping(ApiPaths.COUNT_NON_RESIDENTIAL_ROOM)
    public ResponseEntity<?> countNonResidentialRoom() {
        return service.countNonResidentialRoom();
    }

    // get all ownerships by apartment -------------------------
    @GetMapping(ApiPaths.APARTMENT + ApiPaths.PARAM_APARTMENT)
    public ResponseEntity<?> getAllOwnershipByApartment(@PathVariable String apartment) {
        return service.getAllOwnershipByApartment(apartment);
    }

    // get all personal accounts by apartment -------------------------
    @GetMapping(ApiPaths.APARTMENT +
            ApiPaths.BILL +
            ApiPaths.PARAM_APARTMENT)
    public ResponseEntity<?> getAllBillByApartment(@PathVariable String apartment) {
        return service.getAllBillByApartment(apartment);
    }

    // get all apartments by full name -----------------------
    @GetMapping(ApiPaths.FULL_NAME + ApiPaths.PARAM_FULL_NAME)
    public ResponseEntity<?> getAllApartmentByFullName(@PathVariable String fullName) {
        return service.getAllApartmentByFullName(fullName);
    }

    // get all apartments by full name -----------------------
    @GetMapping(ApiPaths.BILL + ApiPaths.PARAM_BILL)
    public ResponseEntity<?> getOwnershipByBill(@PathVariable String bill) {
        return service.getOwnershipByBill(bill);
    }

    @GetMapping(ApiPaths.APARTMENT + ApiPaths.BILL)
    public ResponseEntity<?> getMapApartmentListIdAndBill() {
        return service.getMapApartmentListIdAndBill();
    }

}
