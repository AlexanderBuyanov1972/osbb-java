package com.example.osbb.controller.owner;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.entity.owner.PlaceWork;
import com.example.osbb.service.placework.IPlaceWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.PLACE_WORK)
public class PlaceWorkController {

    @Autowired
    private IPlaceWorkService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createPlaceWork(@RequestBody PlaceWork placeWork) {
        return response.returnResponse(service.createPlaceWork(placeWork));
    }

    @PutMapping
    public ResponseEntity<?> updatePlaceWork(@RequestBody PlaceWork placeWork) {
        return response.returnResponse(service.updatePlaceWork(placeWork));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getPlaceWork(@PathVariable Long id) {
        return response.returnResponse(service.getPlaceWork(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deletePlaceWork(@PathVariable Long id) {
        return response.returnResponse(service.deletePlaceWork(id));
    }

    // ---------------------- all ----------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllPlaceWork(@RequestBody List<PlaceWork> list) {
        return response.returnResponse(service.createAllPlaceWork(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllPlaceWork(@RequestBody List<PlaceWork> list) {
        return response.returnResponse(service.updateAllPlaceWork(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllPlaceWork() {
        return response.returnResponse(service.getAllPlaceWork());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllPlaceWork() {
        return response.returnResponse(service.deleteAllPlaceWork());
    }

}
