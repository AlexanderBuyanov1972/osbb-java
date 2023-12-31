package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.service.ownership.IOwnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.OWNERSHIP)
public class OwnershipController {

    @Autowired
    private IOwnershipService service;

    @Autowired
    private HelpMethodsForController response;

    // ---------- one -------------------

    @PostMapping
    public ResponseEntity<?> createOwnership(@RequestBody Ownership one) {
        return response.returnResponse(service.createOwnership(one));
    }

    @PutMapping
    public ResponseEntity<?> updateOwnership(@RequestBody Ownership one) {
        return response.returnResponse(service.updateOwnership(one));
    }

    @GetMapping(ApiConstants.PARAM_ID)
    public ResponseEntity<?> getOwnership(@PathVariable Long id) {
        return response.returnResponse(service.getOwnership(id));
    }

    @DeleteMapping(ApiConstants.PARAM_ID)
    public ResponseEntity<?> deleteOwnership(@PathVariable Long id) {
        return response.returnResponse(service.deleteOwnership(id));
    }

    // ------------------- all ----------------
    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllOwnership(@RequestBody List<Ownership> list) {
        return response.returnResponse(service.createAllOwnership(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllOwnership(@RequestBody List<Ownership> list) {
        return response.returnResponse(service.updateAllOwnership(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllOwnership() {
        return response.returnResponse(service.getAllOwnership());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllOwnership() {
        return response.returnResponse(service.deleteAllOwnership());
    }

    // ------------- summa areas ------------

    // Общая площадь жилых и нежилых помещений
    @GetMapping(ApiConstants.SUMMA_AREA_ROOMS)
    public ResponseEntity<?> summaAreaRooms() {
        return response.returnResponse(service.summaAreaRooms());
    }

    // Площадь квартир
    @GetMapping(ApiConstants.SUMMA_AREA_APARTMENT)
    public ResponseEntity<?> summaAreaApartment() {
        return response.returnResponse(service.summaAreaApartment());
    }

    //  Жилая площадь квартир
    @GetMapping(ApiConstants.SUMMA_AREA_LIVING_APARTMENT)
    public ResponseEntity<?> summaAreaLivingApartment() {
        return response.returnResponse(service.summaAreaLivingApartment());
    }

    // Площадь нежилых помещений
    @GetMapping(ApiConstants.SUMMA_AREA_NON_RESIDENTIAL_ROOM)
    public ResponseEntity<?> summaAreaNonResidentialRoom() {
        return response.returnResponse(service.summaAreaNonResidentialRoom());
    }

    // ------------- count rooms -----------------

    // Количество помещений
    @GetMapping(ApiConstants.COUNT_ROOMS)
    public ResponseEntity<?> countRooms() {
        return response.returnResponse(service.countRooms());
    }

    // Количество квартир
    @GetMapping(ApiConstants.COUNT_APARTMENT)
    public ResponseEntity<?> countApartment() {
        return response.returnResponse(service.countApartment());
    }

    // Количество нежилых помещений -----------------------
    @GetMapping(ApiConstants.COUNT_NON_RESIDENTIAL_ROOM)
    public ResponseEntity<?> countNonResidentialRoom() {
        return response.returnResponse(service.countNonResidentialRoom());
    }

    // get all ownerships by apartment -------------------------
    @GetMapping(ApiConstants.APARTMENT + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> getAllOwnershipByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.getAllOwnershipByApartment(apartment));
    }

    // get all personal accounts by apartment -------------------------
    @GetMapping(ApiConstants.APARTMENT +
            ApiConstants.BILL +
            ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> getAllBillByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.getAllBillByApartment(apartment));
    }

    // get all apartments by full name -----------------------
    @GetMapping(ApiConstants.FULL_NAME + ApiConstants.PARAM_FULL_NAME)
    public ResponseEntity<?> getAllApartmentByFullName(@PathVariable String fullName) {
        return response.returnResponse(service.getAllApartmentByFullName(fullName));
    }

    // get all apartments by full name -----------------------
    @GetMapping(ApiConstants.BILL + ApiConstants.PARAM_BILL)
    public ResponseEntity<?> getOwnershipByBill(@PathVariable String bill) {
        return response.returnResponse(service.getOwnershipByBill(bill));
    }

    @GetMapping(ApiConstants.APARTMENT + ApiConstants.BILL)
    public ResponseEntity<?> getMapApartmentListIdAndBill() {
        return response.returnResponse(service.getMapApartmentListIdAndBill());
    }

}
