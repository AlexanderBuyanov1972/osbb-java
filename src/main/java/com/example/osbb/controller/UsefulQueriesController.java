package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.service.queries.IQueriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ApiPaths.QUERIES)
public class UsefulQueriesController {

    @Autowired
    private IQueriesService service;

    // печатать объявление о новых реквизитах по оплате за услуги ОСББ
    @GetMapping(ApiPaths.PAYMENT + ApiPaths.NEW_BILL)
    public ResponseEntity<?> queryNewBillForPayServiceOSBB() {
        return service.queryNewBillForPayServiceOSBB();
    }

    @GetMapping(ApiPaths.APARTMENT + ApiPaths.HEAT_SUPPLY)
    public ResponseEntity<?> queryListHeatSupplyForApartment() {
        return service.queryListHeatSupplyForApartment();
    }

    @GetMapping(ApiPaths.REPORT)
    public ResponseEntity<?> queryReport_2023_11() {
        return service.queryReport_2023_11();
    }

    @GetMapping(ApiPaths.APARTMENT + ApiPaths.BILL+ ApiPaths.FULL_NAME)
    public ResponseEntity<?> queryListApartmentBillFullNamePhoneNumber() {
        return service.queryListApartmentBillFullNamePhoneNumber();
    }

    @GetMapping(ApiPaths.PAYMENT + ApiPaths.DEBT + ApiPaths.BALANCE)
    public ResponseEntity<?> queryBalanceHouse() {
        return service.queryBalanceHouse();
    }

}
