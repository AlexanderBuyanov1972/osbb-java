package com.example.osbb.controller.owner;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.owner.Vehicle;
import com.example.osbb.service.vehicle.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.VEHICLE)
public class VehicleController {
    @Autowired
    private IVehicleService service;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody Vehicle vehicle) {
        return service.createVehicle(vehicle);
    }

    @PutMapping
    public ResponseEntity<?> updateVehicle(@RequestBody Vehicle vehicle) {
        return service.updateVehicle(vehicle);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getVehicle(@PathVariable Long id) {
        return service.getVehicle(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        return service.deleteVehicle(id);
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllVehicle(@RequestBody List<Vehicle> list) {
        return service.createAllVehicle(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllVehicle(@RequestBody List<Vehicle> list) {
        return service.updateAllVehicle(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllVehicle() {
        return service.getAllVehicle();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllVehicle() {
        return service.deleteAllVehicle();
    }

    // ----------- get Vehicle By Number Vehicle ------------

    @GetMapping(ApiPaths.REGISTRATION_NUMBER + ApiPaths.PARAM_ID)
    public ResponseEntity<?> getVehicleByNumberVehicle(@PathVariable String numberVehicle) {
        return service.getVehicleByNumberVehicle(numberVehicle);
    }
}
