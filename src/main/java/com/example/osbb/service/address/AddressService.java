package com.example.osbb.service.address;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dto.ErrorResponseMessages;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ResponseMessages;
import com.example.osbb.entity.Address;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Object createAddress(Address address) {
        try {
            List<String> errors = new ArrayList<>();
            if (addressDAO.existsById(address.getId()))
                errors.add(ServiceMessages.ALREADY_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(addressDAO.save(address))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAddress(Address address) {
        try {
            List<String> errors = new ArrayList<>();
            if (!addressDAO.existsById(address.getId()))
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(addressDAO.save(address))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    @Override
    public Object getAddress(Long id) {
        try {
            List<String> errors = new ArrayList<>();
            if (!addressDAO.existsById(id)) {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(addressDAO.findById(id).get())
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }


    @Override
    @Transactional
    public Object deleteAddress(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (addressDAO.existsById(id)) {
                addressDAO.deleteById(id);
            } else {
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }


    // ---------------- all ----------------

    @Override
    @Transactional
    public Object createAllAddress(List<Address> addresses) {
        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (!addressDAO.existsById(address.getId())) {
                    result.add(addressDAO.save(address));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllAddress(List<Address> addresses) {
        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (addressDAO.existsById(address.getId())) {
                    result.add(addressDAO.save(address));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(ServiceMessages.OK))
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
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(returnListSortedById(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllAddress() {
        try {
            addressDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // -------- street. house and number of apartment -----------------------

    @Override
    public Object getAddress(String street, String house, String numberApartment) {
        try {
            if (!addressDAO.existsByStreetAndHouseAndApartment(street, house, numberApartment))
                return new ErrorResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
            return Response.builder()
                    .data(addressDAO.findByStreetAndHouseAndApartment(street, house, numberApartment))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // sorted ------------------------------
    private List<Address> returnListSortedById(List<Address> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Address> returnListSortedDyId(List<Address> list) {
        return list.stream()
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .collect(Collectors.toList());
    }

}
