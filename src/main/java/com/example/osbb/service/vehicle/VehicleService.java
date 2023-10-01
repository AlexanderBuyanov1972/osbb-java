package com.example.osbb.service.vehicle;

import com.example.osbb.dao.VehicleDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.Vehicle;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService implements IVehicleService {
    @Autowired
    private VehicleDAO vehicleDAO;

    @Override
    @Transactional
    public Object createVehicle(Vehicle vehicle) {
        try {
            List<String> errors = new ArrayList<>();
            if (vehicleDAO.existsById(vehicle.getId()))
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.save(vehicle
                    ))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateVehicle(Vehicle vehicle) {
        try {
            List<String> errors = new ArrayList<>();
            if (!vehicleDAO.existsById(vehicle.getId()))
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.save(vehicle
                    ))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getVehicle(Long id) {
        try {
            List<String> errors = new ArrayList<>();
            if (!vehicleDAO.existsById(id)) {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.findById(id).get())
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteVehicle(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (vehicleDAO.existsById(id)) {
                vehicleDAO.deleteById(id);
            } else {
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (IllegalArgumentException exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllVehicle(List<Vehicle> vehicles) {
        List<Vehicle> result = new ArrayList<>();
        try {
            for (Vehicle vehicle : vehicles) {
                if (!vehicleDAO.existsById(vehicle.getId()))
                    result.add(vehicleDAO.save(vehicle));
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllVehicle(List<Vehicle> vehicles) {
        List<Vehicle> result = new ArrayList<>();
        try {
            for (Vehicle vehicle : vehicles) {
                if (vehicleDAO.existsById(vehicle.getId())
                        && vehicleDAO.existsByNumberVehicle(vehicle.getNumberVehicle())) {
                    result.add(vehicleDAO.save(vehicle));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllVehicle() {
        try {
            List<Vehicle> result = vehicleDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllVehicle() {
        try {
            vehicleDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getVehicleByNumberVehicle(String numberVehicle) {
        try {
            List<String> errors = new ArrayList<>();
            if (!vehicleDAO.existsByNumberVehicle(numberVehicle)) {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.findByNumberVehicle(numberVehicle))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // sorted -----------------
    private List<Vehicle> returnListSorted(List<Vehicle> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
