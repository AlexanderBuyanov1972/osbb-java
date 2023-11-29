package com.example.osbb.service.vehicle;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.VehicleDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.owner.Vehicle;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService implements IVehicleService {
    private static final Logger log = Logger.getLogger(VehicleService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private VehicleDAO vehicleDAO;

    @Override
    public Object createVehicle(Vehicle vehicle) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Транспортное средство создано успешно";
        log.info(messageEnter(methodName));
        try {
            vehicle = vehicleDAO.save(vehicle);
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(vehicle)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateVehicle(Vehicle vehicle) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Транспортное средство с ID : " + vehicle.getId() + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (vehicleDAO.existsById(vehicle.getId())) {
                vehicle = vehicleDAO.save(vehicle);
                messageResponse = "Транспортное средство обновлено успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(vehicle, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getVehicle(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Транспортное средство с ID : " + id + " не существует";

        log.info("Method getVehicle : enter");
        try {
            Vehicle vehicle = vehicleDAO.findById(id).orElse(null);
            if (vehicle != null)
                messageResponse = "Транспортное средство c ID : " + id + " получено успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(vehicle)
                    .messages(List.of())
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteVehicle(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Транспортное средство с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (vehicleDAO.existsById(id)) {
                vehicleDAO.deleteById(id);
                messageResponse = "Транспортное средство с ID : " + id + " удалено успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(id, List.of(messageResponse));
        } catch (IllegalArgumentException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllVehicle(List<Vehicle> vehicles) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не создано ни одного транспортного средства";
        log.info(messageEnter(methodName));
        List<Vehicle> result = new ArrayList<>();
        try {
            for (Vehicle vehicle : vehicles) {
                if (!vehicleDAO.existsById(vehicle.getId()))
                    vehicle = vehicleDAO.save(vehicle);
                log.info("Создано транспортное средство с ID : " + vehicle.getId());
                result.add(vehicle);
            }
            messageResponse = result.isEmpty() ? messageResponse : "Создано " + result.size() + " транспортных средств";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(listSorted(result), List.of(messageResponse));
        } catch (
                Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllVehicle(List<Vehicle> vehicles) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не обновлено ни одного транспортного средства";
        log.info(messageEnter(methodName));
        List<Vehicle> result = new ArrayList<>();
        try {
            for (Vehicle vehicle : vehicles) {
                if (vehicleDAO.existsById(vehicle.getId())
                        && vehicleDAO.existsByNumberVehicle(vehicle.getNumberVehicle())) {
                    result.add(vehicleDAO.save(vehicle));
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Обновлено " + result.size() + " транспортных средств";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(listSorted(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAllVehicle() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Vehicle> result = vehicleDAO.findAll();
            String messageResponse = "Получены " + result.size() + " транспортных средств";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(listSorted(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllVehicle() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            vehicleDAO.deleteAll();
            String messageResponse = "Удалены все транспортные средства";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getVehicleByNumberVehicle(String numberVehicle) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Транспортное средство с номером : " + numberVehicle + " не существует";
        try {
            Vehicle vehicle = vehicleDAO.findByNumberVehicle(numberVehicle);
            if (vehicle != null)
                messageResponse = "Получение транспортного средства прошло успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(vehicle, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // sorted -----------------
    private List<Vehicle> listSorted(List<Vehicle> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
