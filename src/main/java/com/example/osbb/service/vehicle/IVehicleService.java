package com.example.osbb.service.vehicle;

import com.example.osbb.entity.Address;
import com.example.osbb.entity.Vehicle;

import java.util.List;

public interface IVehicleService {
    // ------------------- one -----------------------
    public Object createVehicle(Vehicle vehicle);

    public Object updateVehicle(Vehicle vehicle);

    public Object getVehicle(Long id);

    public Object deleteVehicle(Long id);

    // ------------------ all ----------------

    public Object createAllVehicle(List<Vehicle> list);

    public Object updateAllVehicle(List<Vehicle> list);

    public Object getAllVehicle();

    public Object deleteAllVehicle();

    // ------------- number vehicle ----------------

    public Object getVehicleByNumberVehicle(String numberVehicle);
}
