package com.example.osbb.service.address;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Address;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService implements IAddressService {
    @Autowired
    private AddressDAO addressDAO;

    // ----------------- one -------------------------------

    @Override
    public Object createAddress(Address address) {
        try {
            List<String> errors = new ArrayList<>();
            if (addressDAO.existsById(address.getId()))
                errors.add("Адресс с таким ID уже существует.");
            if (addressDAO.existsByStreetAndHouseAndApartment(
                    address.getStreet(),
                    address.getHouse(),
                    address.getApartment()
            ))
                errors.add("Адресс с такой улицей, номером дома и номером квартиры уже существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(addressDAO.save(address))
                    .messages(List.of("Создание адресса прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object updateAddress(Address address) {
        try {
            List<String> errors = new ArrayList<>();
            if (!addressDAO.existsById(address.getId()))
                errors.add("Адресс с таким ID не существует.");
            if (!addressDAO.existsByStreetAndHouseAndApartment(
                    address.getStreet(),
                    address.getHouse(),
                    address.getApartment()
            ))
                errors.add("Адресс с такой улицей, номером дома и номером квартиры не существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(addressDAO.save(address))
                    .messages(List.of("Обновление адресса прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object getAddress(Long id) {
        try {
            List<String> list = new ArrayList<>();
            if (!addressDAO.existsById(id)) {
                list.add("Адресс с таким ID не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(addressDAO.findById(id).get())
                    .messages(List.of("Получение адресса прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }


    @Override
    public Object deleteAddress(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (addressDAO.existsById(id)) {
                addressDAO.deleteById(id);
            } else {
                list.add("Адресс с таким ID не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of("Удаление адресса прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    // ---------------- all ----------------

    @Override
    public Object createAllAddress(List<Address> addresses) {
        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (!addressDAO.existsById(address.getId())) {
                    result.add(addressDAO.save(address));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из адрессов создан не был. Адресса с такими ID уже существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создан " + result.size() + " адресс из " + addresses.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateAllAddress(List<Address> addresses) {
        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (addressDAO.existsById(address.getId())) {
                    result.add(addressDAO.save(address));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни один из адрессов обновлён не был. Адресса с такими ID не существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлен " + result.size() + " адресс из " + addresses.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllAddress() {
        try {
            List<Address> result = addressDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of("В базе данных нет ни одного адресса по вашему запросу."))
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
    public Object deleteAllAddress() {
        try {
            addressDAO.deleteAll();
            return new ResponseMessages(List.of("Все адресса удалены успешно.", "Удачного дня!"));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // -------- street. house and number of apartment -----------------------

    @Override
    public Object getAddress(String street, String house, String numberApartment) {
        try {
            if (!addressDAO.existsByStreetAndHouseAndApartment(street, house, numberApartment))
                return new ErrorResponseMessages(List.of("Адресс с такой улицей, номером дома и номером квартиры не существует."));
            return Response.builder()
                    .data(addressDAO.findByStreetAndHouseAndApartment(street, house, numberApartment))
                    .messages(List.of("Адресс с такой улицей, номером дома и номером квартиры успешно получен.","Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Address> returnListSorted(List<Address> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
