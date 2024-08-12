package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.Rate;
import com.example.osbb.service.rate.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.RATE)
public class RateController {

    @Autowired
    private IRateService service;

    // one -----------------------------------------------

    @PostMapping
    public ResponseEntity<?> createRate(@RequestBody Rate rate) {
        return service.createRate(rate);
    }

    @PutMapping
    public ResponseEntity<?> updateRate(@RequestBody Rate rate) {
        return service.updateRate(rate);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getRate(@PathVariable Long id) {
        return service.getRate(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deleteRate(@PathVariable Long id) {
        return service.deleteRate(id);
    }

    // all -----------------------------------------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllRate(@RequestBody List<Rate> list) {
        return service.createAllRate(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllRate(@RequestBody List<Rate> list) {
        return service.updateAllRate(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllRate() {
        return service.getAllRate();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllRate() {
        return service.deleteAllRate();
    }

}
