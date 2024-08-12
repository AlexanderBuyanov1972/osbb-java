package com.example.osbb.controller.owner;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.owner.PlaceWork;
import com.example.osbb.service.placework.IPlaceWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.PLACE_WORK)
public class PlaceWorkController {

    @Autowired
    private IPlaceWorkService service;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createPlaceWork(@RequestBody PlaceWork placeWork) {
        return service.createPlaceWork(placeWork);
    }

    @PutMapping
    public ResponseEntity<?> updatePlaceWork(@RequestBody PlaceWork placeWork) {
        return service.updatePlaceWork(placeWork);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getPlaceWork(@PathVariable Long id) {
        return service.getPlaceWork(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deletePlaceWork(@PathVariable Long id) {
        return service.deletePlaceWork(id);
    }

    // ---------------------- all ----------------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllPlaceWork(@RequestBody List<PlaceWork> list) {
        return service.createAllPlaceWork(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllPlaceWork(@RequestBody List<PlaceWork> list) {
        return service.updateAllPlaceWork(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllPlaceWork() {
        return service.getAllPlaceWork();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllPlaceWork() {
        return service.deleteAllPlaceWork();
    }

}
