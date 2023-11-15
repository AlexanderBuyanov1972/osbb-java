package com.example.osbb.service.vehicle;

import com.example.osbb.dao.owner.VehicleDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Vehicle;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService implements IVehicleService {
    private static final Logger log = LogManager.getLogger("VehicleService");
    @Autowired
    private VehicleDAO vehicleDAO;

    @Override
    public Object createVehicle(Vehicle vehicle) {
        log.info("Method createVehicle : enter");
        try {
            vehicle = vehicleDAO.save(vehicle);
            log.info("Транспортное средство создано успешно");
            log.info("Method createVehicle : exit");
            return Response
                    .builder()
                    .data(vehicle)
                    .messages(List.of("Транспортное средство создано успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateVehicle(Vehicle vehicle) {
        log.info("Method updateVehicle : enter");
        try {
            List<String> errors = new ArrayList<>();
            if (!vehicleDAO.existsById(vehicle.getId())) {
                log.info("Транспортное средство с ID : " + vehicle.getId() + " не существует");
                errors.add("Транспортное средство с ID : " + vehicle.getId() + " не существует");
            } else {
                vehicle = vehicleDAO.save(vehicle);
                log.info("Транспортное средство обновлено успешно");
            }
            log.info("Method updateVehicle : exit");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicle)
                    .messages(List.of("Транспортное средство обновлено успешно"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getVehicle(Long id) {
        Vehicle vehicle = null;
        log.info("Method getVehicle : enter");
        try {
            List<String> errors = new ArrayList<>();
            if (!vehicleDAO.existsById(id)) {
                log.info("Транспортное средство с ID : " + id + " не существует");
                errors.add("Транспортное средство с ID : " + id + " не существует");
            } else {
                vehicle = vehicleDAO.findById(id).get();
                log.info("Транспортное средство c ID : " + id + " получено успешно");
            }
            log.info("Method getVehicle : exit");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicle)
                    .messages(List.of("Транспортное средство c ID : " + id + " получено успешно"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteVehicle(Long id) {
        log.info("Method deleteVehicle : enter");
        List<String> list = new ArrayList<>();
        try {
            if (vehicleDAO.existsById(id)) {
                vehicleDAO.deleteById(id);
                log.info("Транспортное средство с ID : " + id + " удалено успешно");
            } else {
                log.info("Транспортное средство с ID : " + id + " не существует");
                list.add("Транспортное средство с ID : " + id + " не существует");
            }
            log.info("Method deleteVehicle : exit");
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of("Транспортное средство с ID : " + id + " удалено успешно"))
                    .build() : new ResponseMessages(list);
        } catch (IllegalArgumentException error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllVehicle(List<Vehicle> vehicles) {
        log.info("Method createAllVehicle : enter");
        List<Vehicle> result = new ArrayList<>();
        try {
            for (Vehicle vehicle : vehicles) {
                if (!vehicleDAO.existsById(vehicle.getId()))
                    result.add(vehicleDAO.save(vehicle));
            }
            if (result.isEmpty()) {
                log.info("Не создано ни одного транспортного средства");
                log.info("Method createAllVehicle : exit");
                return new ResponseMessages(List
                        .of("Не создано ни одного транспортного средства"));
            }
            log.info("Создано " + result.size() + " транспортных средств");
            log.info("Method createAllVehicle : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Создано " + result.size() + " транспортных средств"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllVehicle(List<Vehicle> vehicles) {
        log.info("Method updateAllVehicle : enter");
        List<Vehicle> result = new ArrayList<>();
        try {
            for (Vehicle vehicle : vehicles) {
                if (vehicleDAO.existsById(vehicle.getId())
                        && vehicleDAO.existsByNumberVehicle(vehicle.getNumberVehicle())) {
                    result.add(vehicleDAO.save(vehicle));
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлено ни одного транспортного средства");
                log.info("Method updateAllVehicle : exit");
                return new ResponseMessages(List
                        .of("Не обновлено ни одного транспортного средства"));
            }
            log.info("Обновлено " + result.size() + " транспортных средств");
            log.info("Method updateAllVehicle : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Обновлено " + result.size() + " транспортных средств"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAllVehicle() {
        log.info("Method getAllVehicle : enter");
        try {
            List<Vehicle> result = vehicleDAO.findAll();
            log.info("Получены " + result.size() + " транспортных средств");
            log.info("Method getAllVehicle : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Получены " + result.size() + " транспортных средств"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllVehicle() {
        log.info("Method deleteAllVehicle : enter");
        try {
            vehicleDAO.deleteAll();
            log.info("Удалены все транспортные средства");
            log.info("Method deleteAllVehicle : exit");
            return new ResponseMessages(List.of("Удалены все транспортные средства"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getVehicleByNumberVehicle(String numberVehicle) {
        log.info("Method getVehicleByNumberVehicle : enter");
        Vehicle vehicle = null;
        try {
            if (!vehicleDAO.existsByNumberVehicle(numberVehicle)) {
                log.info("Транспортное средство с номером : " + numberVehicle + " не существует");
                return new ResponseMessages(List.of("Транспортное средство с номером : " + numberVehicle + " не существует"));
            } else {
                vehicle = vehicleDAO.findByNumberVehicle(numberVehicle);
                log.info("Получение транспортного средства прошло успешно");
                log.info("Method getVehicleByNumberVehicle : exit");
                return Response
                        .builder()
                        .data(vehicle)
                        .messages(List.of("Получение транспортного средства прошло успешно"))
                        .build();
            }
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // sorted -----------------
    private List<Vehicle> listSorted(List<Vehicle> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
