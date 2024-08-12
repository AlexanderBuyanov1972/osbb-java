package com.example.osbb.service.vehicle;

import com.example.osbb.dao.owner.VehicleDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.Vehicle;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VehicleService implements IVehicleService {

    @Autowired
    private VehicleDAO vehicleDAO;

    @Override
    public ResponseEntity<?> createVehicle(Vehicle vehicle) {
        vehicle = vehicleDAO.save(vehicle);
        String message = "Транспортное средство с ID : " + vehicle.getId() + " создано успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(vehicle, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateVehicle(Vehicle vehicle) {
        String message = "Транспортное средство с ID : " + vehicle.getId() + " не существует";
        if (vehicleDAO.existsById(vehicle.getId())) {
            vehicle = vehicleDAO.save(vehicle);
            message = "Транспортное средство с ID : " + vehicle.getId() + " обновлено успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(vehicle, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getVehicle(Long id) {
        String message = "Транспортное средство с ID : " + id + " не существует";
        Vehicle vehicle = vehicleDAO.findById(id).orElse(null);
        if (vehicle == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of()));
        }
        message = "Транспортное средство c ID : " + id + " получено успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(vehicle, List.of()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteVehicle(Long id) {
        String message = "Транспортное средство с ID : " + id + " не существует";
        if (vehicleDAO.existsById(id)) {
            vehicleDAO.deleteById(id);
            message = "Транспортное средство с ID : " + id + " удалено успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> createAllVehicle(List<Vehicle> vehicles) {
        String message = "Не создано ни одного транспортного средства";
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (!vehicleDAO.existsById(vehicle.getId()))
                vehicle = vehicleDAO.save(vehicle);
            log.info("Создано транспортное средство с ID : {}", vehicle.getId());
            result.add(vehicle);
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " транспортных средств";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllVehicle(List<Vehicle> vehicles) {
        String message = "Не обновлено ни одного транспортного средства";
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicleDAO.existsById(vehicle.getId())
                    && vehicleDAO.existsByNumberVehicle(vehicle.getNumberVehicle())) {
                result.add(vehicleDAO.save(vehicle));
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " транспортных средств";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllVehicle() {
        List<Vehicle> result = vehicleDAO.findAll();
        String message = "Получены " + result.size() + " транспортных средств";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllVehicle() {
        vehicleDAO.deleteAll();
        String message = "Удалены все транспортные средства";
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getVehicleByNumberVehicle(String numberVehicle) {
        String message = "Транспортное средство с номером : " + numberVehicle + " не существует";
        Vehicle vehicle = vehicleDAO.findByNumberVehicle(numberVehicle);
        if (vehicle == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Получение транспортного средства прошло успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(vehicle, List.of(message)));
    }

    // sorted -----------------
    private List<Vehicle> listSorted(List<Vehicle> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
