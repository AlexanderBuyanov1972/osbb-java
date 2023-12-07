package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.service.queries.IQueriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ApiConstants.QUERIES)
public class UsefulQueriesController {

    @Autowired
    private IQueriesService iQueriesService;

    @Autowired
    private HelpMethodsForController response;
    // печатать объявление о новых реквизитах по оплате за услуги ОСББ
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.NEW_BILL)
    public ResponseEntity<?> queryNewBillForPayServiceOSBB() {
        return response.returnResponse(iQueriesService.queryNewBillForPayServiceOSBB());
    }

    @GetMapping(ApiConstants.APARTMENT + ApiConstants.HEAT_SUPPLY)
    public ResponseEntity<?> queryListHeatSupplyForApartment() {
        return response.returnResponse(iQueriesService.queryListHeatSupplyForApartment());
    }

    @GetMapping(ApiConstants.REPORT)
    public ResponseEntity<?> queryReport_2023_11() {
        return response.returnResponse(iQueriesService.queryReport_2023_11());
    }

    @GetMapping(ApiConstants.APARTMENT + ApiConstants.BILL+ ApiConstants.FULL_NAME)
    public ResponseEntity<?> queryListApartmentBillFullNamePhoneNumber() {
        return response.returnResponse(iQueriesService.queryListApartmentBillFullNamePhoneNumber());
    }

    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.BALANCE)
    public ResponseEntity<?> queryBalanceHouse() {
        return response.returnResponse(iQueriesService.queryBalanceHouse());
    }

}
