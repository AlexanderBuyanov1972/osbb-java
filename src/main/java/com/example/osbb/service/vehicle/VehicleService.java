package com.example.osbb.service.vehicle;

import com.example.osbb.dao.VehicleDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Address;
import com.example.osbb.entity.Vehicle;
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
                errors.add("Транспортное средство с ID = " + vehicle.getId() + " уже существует.");
            if (vehicleDAO.existsByNumberVehicle(vehicle.getNumberVehicle()))
                errors.add("Транспортное средство c номером = " + vehicle.getNumberVehicle() + " уже существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.save(vehicle
                    ))
                    .messages(List.of("Создание транспортного средства c ID = " + vehicle.getId() + " прошло успешно.", "Удачного дня!"))
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
                errors.add("Транспортное средство с ID = " + vehicle.getId() + " не существует.");
            if (!vehicleDAO.existsByNumberVehicle(vehicle.getNumberVehicle()))
                errors.add("Транспортное средство c номером = " + vehicle.getNumberVehicle() + " не существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.save(vehicle
                    ))
                    .messages(List.of("Обновление транспортного средства c ID = " + vehicle.getId() + " прошло успешно.", "Удачного дня!"))
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
                errors.add("Транспортное средство с ID = " + id + " не существует.");
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.findById(id).get())
                    .messages(List.of("Получение транспортного средства c ID = " + id + " прошло успешно.", "Удачного дня!"))
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
                list.add("Транспортное средство с ID = " + id + " не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of("Удаление транспортного средства c ID = " + id + " прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllVehicle(List<Vehicle> vehicles) {
        List<Vehicle> result = new ArrayList<>();
        try {
            for (Vehicle vehicle : vehicles) {
                if (!vehicleDAO.existsById(vehicle.getId())
                        && !vehicleDAO.existsByNumberVehicle(vehicle.getNumberVehicle())) {
                    result.add(vehicleDAO.save(vehicle));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни одно из транспортных средств создано не было. транспортные средства с такими ID уже существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно созданы " + result.size() + " транспортные средства из " + vehicles.size() + ".", "Удачного дня!"))
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
                    .of("Ни одно из транспортных средств обновлено не было. транспортные средства с такими ID не существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлены " + result.size() + " транспортные средства из " + vehicles.size() + ".",
                            "Удачного дня!"))
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
                    new ResponseMessages(List.of("В базе данных нет ни одного транспортного средства по вашему запросу."))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of("Запрос выполнен успешно.", "Удачного дня!"))
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
            return new ResponseMessages(List.of("Все транспортные средства удалены успешно.", "Удачного дня!"));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getVehicleByNumberVehicle(String numberVehicle) {
        try {
            List<String> errors = new ArrayList<>();
            if (!vehicleDAO.existsByNumberVehicle(numberVehicle)) {
                errors.add("Транспортное средство с номером = " + numberVehicle + " не существует.");
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(vehicleDAO.findByNumberVehicle(numberVehicle))
                    .messages(List.of("Получение транспортного средства c номером = " + numberVehicle + " прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    private List<Vehicle> returnListSorted(List<Vehicle> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
