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
    private IQueriesService service;

    @Autowired
    private HelpMethodsForController response;

    @GetMapping(ApiConstants.APARTMENT + ApiConstants.HEAT_SUPPLY)
    public ResponseEntity<?> queryListHeatSupplyForApartment() {
        return response.returnResponse(service.queryListHeatSupplyForApartment());
    }

}
