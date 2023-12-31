package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.entity.Rate;
import com.example.osbb.service.rate.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.RATE)
public class RateController {

    @Autowired
    private IRateService service;

    @Autowired
    private HelpMethodsForController response;

    // one -----------------------------------------------

    @PostMapping
    public ResponseEntity<?> createRate(@RequestBody Rate rate) {
        return response.returnResponse(service.createRate(rate));
    }

    @PutMapping
    public ResponseEntity<?> updateRate(@RequestBody Rate rate) {
        return response.returnResponse(service.updateRate(rate));
    }

    @GetMapping(ApiConstants.PARAM_ID)
    public ResponseEntity<?> getRate(@PathVariable Long id) {
        return response.returnResponse(service.getRate(id));
    }

    @DeleteMapping(ApiConstants.PARAM_ID)
    public ResponseEntity<?> deleteRate(@PathVariable Long id) {
        return response.returnResponse(service.deleteRate(id));
    }

    // all -----------------------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllRate(@RequestBody List<Rate> list) {
        return response.returnResponse(service.createAllRate(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllRate(@RequestBody List<Rate> list) {
        return response.returnResponse(service.updateAllRate(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllRate() {
        return response.returnResponse(service.getAllRate());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllRate() {
        return response.returnResponse(service.deleteAllRate());
    }

}
