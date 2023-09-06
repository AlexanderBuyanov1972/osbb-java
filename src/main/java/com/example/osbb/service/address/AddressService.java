package com.example.osbb.service.address;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Address;
import com.example.osbb.dto.messages.ErrorResponseMessages;
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
            List<String> list = new ArrayList<>();
            if (addressDAO.existsById(address.getId()))
                list.add("Адресс с таким ID уже существует.");
            if (addressDAO.existsByStreetAndHouseAndApartment(
                    address.getStreet(),
                    address.getHouse(),
                    address.getApartment()
            ))
                list.add("Адресс с такой улицей, номером дома и номером квартиры уже существует.");
            return list.isEmpty() ? List.of(addressDAO.save(address)) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object updateAddress(Address address) {
        try {
            List<String> list = new ArrayList<>();
            if (!addressDAO.existsById(address.getId()))
                list.add("Адресс с таким ID не существует.");
            if (!addressDAO.existsByStreetAndHouseAndApartment(
                    address.getStreet(),
                    address.getHouse(),
                    address.getApartment()
            ))
                list.add("Адресс с такой улицей, номером дома и номером квартиры не существует.");
            return list.isEmpty() ? List.of(addressDAO.save(address)) : new ErrorResponseMessages(list);
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
            return list.isEmpty() ? List.of(addressDAO.findById(id).get()) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }


    @Override
    public Object deleteAddress(Long id) {
        try {
            if (addressDAO.existsById(id)) {
                addressDAO.deleteById(id);
                return new ResponseMessages(List.of("Адресс удалён успешно."));
            }
            return new ErrorResponseMessages(List.of("Адресс с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    // ---------------- all ----------------

    @Override
    public Object createAllAddress(List<Address> list) {
        try {
            List<Address> result = new ArrayList<>();
            for (Address one : list) {
                if (!addressDAO.existsById(one.getId()) &&
                        !addressDAO.existsByStreetAndHouseAndApartment(
                                one.getStreet(),
                                one.getHouse(),
                                one.getApartment())) {
                    addressDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ErrorResponseMessages(List.
                    of("Ни один из адрессов создан не был. Адресса с такими ID уже существуют."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateAllAddress(List<Address> list) {
        try {
            List<Address> result = new ArrayList<>();
            for (Address one : list) {
                if (addressDAO.existsById(one.getId())) {
                    addressDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ErrorResponseMessages(List
                    .of("Ни один из адрессов обновлён не был. Адресса с такими ID не существуют."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllAddress() {
        try {
            List<Address> result = addressDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных адрессов не существует."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object deleteAllAddress() {
        try {
            addressDAO.deleteAll();
            return new ResponseMessages(List.of("Все адресса удалены успешно."));
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
            return addressDAO.findByStreetAndHouseAndApartment(street, house, numberApartment);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Address> returnListSorted(List<Address> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
