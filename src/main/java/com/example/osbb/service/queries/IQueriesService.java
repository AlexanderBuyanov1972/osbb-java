package com.example.osbb.service.queries;

import org.springframework.http.ResponseEntity;

public interface IQueriesService {

    ResponseEntity<?> queryNewBillForPayServiceOSBB();

    ResponseEntity<?> queryListHeatSupplyForApartment();

    ResponseEntity<?> queryReport_2023_11();

    ResponseEntity<?> queryListApartmentBillFullNamePhoneNumber();

    ResponseEntity<?> queryBalanceHouse();
}
