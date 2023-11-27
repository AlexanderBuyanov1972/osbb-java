package com.example.osbb.service.address;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.ownership.Address;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService implements IAddressService {
    private static final Logger log = Logger.getLogger(AddressService.class);
    @Autowired
    private AddressDAO addressDAO;

    // ----------------- one -------------------------------

    @Override
    @Transactional
    public Object createAddress(Address address) {
        log.info("Method createAddress: enter");
        try {
            address = addressDAO.save(address);
            log.info("Адресс создан успешно");
            log.info("Method createAddress: exit");
            return Response
                    .builder()
                    .data(address)
                    .messages(List.of("Адресс создан успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAddress(Address address) {
        log.info("Method updateAddress: enter");
        try {
            if (!addressDAO.existsById(address.getId())) {
                log.info("Адресс с ID : " + address.getId() + " не существует");
                return new ResponseMessages(List.of("Адресс с ID : " + address.getId() + " не существует"));
            }
            address = addressDAO.save(address);
            log.info("Адресс обновлён успешно");
            log.info("Method updateAddress: exit");
            return Response
                    .builder()
                    .data(address)
                    .messages(List.of("Адресс обновлён успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getAddress(Long id) {
        log.info("Method getAddress: enter");
        try {
            if (!addressDAO.existsById(id)) {
                log.info("Адресс с ID : " + id + " не существует");
                return new ResponseMessages(List.of("Адресс с ID : " + id + " не существует"));
            }
            Address address = addressDAO.findById(id).get();
            log.info("Адресс получен успешно");
            log.info("Method getAddress: exit");
            return Response
                    .builder()
                    .data(address)
                    .messages(List.of("Адресс получен успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAddressStart() {
        try {
            return Response
                    .builder()
                    .data(Address.builder()
                            .zipCode("51931")
                            .country("Украина")
                            .region("Днепропетровская область")
                            .city("Каменское")
                            .street("Свободы")
                            .house("51")
                            .entrance("")
                            .floor("")
                            .apartment("")
                            .build())
                    .messages(List.of("Адресс получен успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }


    @Override
    @Transactional
    public Object deleteAddress(Long id) {
        log.info("Method deleteAddress: enter");
        try {
            if (addressDAO.existsById(id)) {
                addressDAO.deleteById(id);
                log.info("Адресс с ID : " + id + " удалён успешно");
                log.info("Method deleteAddress: exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Адресс с ID : " + id + " удалён успешно"))
                        .build();
            } else {
                log.info("Адресс с ID : " + id + " не существует");
                log.info("Method deleteAddress: exit");
                return new ResponseMessages(List.of("Адресс с ID : " + id + " не существует"));
            }
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // ---------------- all ----------------

    @Override
    @Transactional
    public Object createAllAddress(List<Address> addresses) {
        log.info("Method createAllAddress: enter");
        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (!addressDAO.existsById(address.getId())) {
                    result.add(addressDAO.save(address));
                }
            }
            if (result.isEmpty()) {
                log.info("Не создано ни одного адресса");
                log.info("Method createAllAddress: exit");
                return new ResponseMessages(List.of("Не создано ни одного адресса"));

            }
            log.info("Создано " + result.size() + " адрессов");
            log.info("Method createAllAddress: exit");
            return Response
                    .builder()
                    .data(listSortedById(result))
                    .messages(List.of("Создано " + result.size() + " адрессов"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllAddress(List<Address> addresses) {
        log.info("Method updateAllAddress: enter");
        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (addressDAO.existsById(address.getId()))
                    result.add(addressDAO.save(address));
            }
            if (result.isEmpty()) {
                log.info("Не обновлено ни одного адресса");
                log.info("Method updateAllAddress: exit");
                return new ResponseMessages(List.of("Не обновлено ни одного адресса"));
            }
            log.info("Обновлено " + result.size() + " адрессов");
            log.info("Method updateAllAddress: exit");
            return Response
                    .builder()
                    .data(listSortedById(result))
                    .messages(List.of("Обновлено " + result.size() + " адрессов"))
                    .build();
        } catch (
                Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getAllAddress() {
        log.info("Method getAllAddress: enter");
        try {
            List<Address> result = addressDAO.findAll();
            log.info("Получены все адресса");
            log.info("Method getAllAddress: exit");
            return Response
                    .builder()
                    .data(listSortedById(result))
                    .messages(List.of("Получены все адресса"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllAddress() {
        log.info("Method deleteAllAddress: enter");
        try {
            addressDAO.deleteAll();
            log.info("Удалены все адресса");
            log.info("Method deleteAllAddress: exit");
            return new ResponseMessages(List.of("Удалены все адресса"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    // -------- street. house and number of apartment -----------------------

    @Override
    public Object getAddress(String street, String house, String numberApartment) {
        log.info("Method getAddress: enter");
        try {
            if (!addressDAO.existsByStreetAndHouseAndApartment(street, house, numberApartment)) {
                log.info("По указанным данным адресс не существует");
                log.info("Method getAddress: exit");
                return new ErrorResponseMessages(List.of("По указанным данным адресс не существует"));
            }
            Address address = addressDAO.findByStreetAndHouseAndApartment(street, house, numberApartment);
            log.info("Адресс получен успешно");
            log.info("Method getAddress: exit");
            return Response.builder()
                    .data(address)
                    .messages(List.of("Адресс получен успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // sorted ------------------------------
    private List<Address> listSortedById(List<Address> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Address> listSortedDyId(List<Address> list) {
        return list.stream()
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .collect(Collectors.toList());
    }

}
