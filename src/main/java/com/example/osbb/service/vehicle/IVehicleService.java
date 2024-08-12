package com.example.osbb.service.vehicle;

import com.example.osbb.entity.owner.Vehicle;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IVehicleService {
    // ------------------- one -----------------------
    ResponseEntity<?> createVehicle(Vehicle vehicle);

    ResponseEntity<?> updateVehicle(Vehicle vehicle);

    ResponseEntity<?> getVehicle(Long id);

    ResponseEntity<?> deleteVehicle(Long id);

    // ------------------ all ----------------

    ResponseEntity<?> createAllVehicle(List<Vehicle> list);

    ResponseEntity<?> updateAllVehicle(List<Vehicle> list);

    ResponseEntity<?> getAllVehicle();

    ResponseEntity<?> deleteAllVehicle();

    // ------------- number vehicle ----------------

    ResponseEntity<?> getVehicleByNumberVehicle(String numberVehicle);
}
