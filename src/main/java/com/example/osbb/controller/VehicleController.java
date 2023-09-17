package com.example.osbb.controller;

import com.example.osbb.entity.Vehicle;
import com.example.osbb.service.vehicle.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.VEHICLE)
public class VehicleController {
    @Autowired
    private IVehicleService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody Vehicle vehicle) {
        return response.returnResponse(service.createVehicle(vehicle));
    }

    @PutMapping
    public ResponseEntity<?> updateVehicle(@RequestBody Vehicle vehicle) {
        return response.returnResponse(service.updateVehicle(vehicle));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getVehicle(@PathVariable Long id) {
        return response.returnResponse(service.getVehicle(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        return response.returnResponse(service.deleteVehicle(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllVehicle(@RequestBody List<Vehicle> list) {
        return response.returnResponse(service.createAllVehicle(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllVehicle(@RequestBody List<Vehicle> list) {
        return response.returnResponse(service.updateAllVehicle(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllVehicle() {
        return response.returnResponse(service.getAllVehicle());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllVehicle() {
        return response.returnResponse(service.deleteAllVehicle());
    }

    // ----------- street, house and number apartment ------------

    @GetMapping(ApiConstants.REGISTRATION_NUMBER + ApiConstants.ID)
    public ResponseEntity<?> getVehicleByNumberVehicle( @PathVariable String numberVehicle) {
        return response.returnResponse(service.getVehicleByNumberVehicle(numberVehicle));
    }
}
