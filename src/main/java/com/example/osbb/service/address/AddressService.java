package com.example.osbb.service.address;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.ownership.Address;
import com.example.osbb.service.Comparators;
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
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private AddressDAO addressDAO;
    @Autowired
    private Comparators comparators;

    // ----------------- one -------------------------------
    @Override
    @Transactional
    public Object createAddress(Address address) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            address = addressDAO.save(address);
            String messageResponse = "Адресс с ID : " + address.getId() + " создан успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(address, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAddress(Address address) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Адресс с ID : " + address.getId() + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (addressDAO.existsById(address.getId())) {
                address = addressDAO.save(address);
                messageResponse = "Адресс c ID : " + address.getId() + " обновлён успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(address, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAddress(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Адресс с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            Address address = addressDAO.findById(id).orElse(null);
            if (address != null)
                messageResponse = "Адресс c ID : " + id + " получен успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(address, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAddress(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageSuccessfully = "Адресс c ID : " + id + " удалён успешно";
        String messageResponse = "Адресс с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (addressDAO.existsById(id)) {
                addressDAO.deleteById(id);
                messageResponse = messageSuccessfully;
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(id, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // ---------------- all ----------------
    @Override
    @Transactional
    public Object createAllAddress(List<Address> addresses) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не создано ни одного адресса";
        log.info(messageEnter(methodName));

        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (!addressDAO.existsById(address.getId())) {
                    address = addressDAO.save(address);
                    log.info("Адресс с № помещения : " + address.getApartment() + " создан успешно");
                    result.add(address);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Создано " + result.size() + " адрессов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(comparators.sortedAddressById(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllAddress(List<Address> addresses) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не обновлено ни одного адресса";
        log.info(messageEnter(methodName));

        List<Address> result = new ArrayList<>();
        try {
            for (Address address : addresses) {
                if (addressDAO.existsById(address.getId()))
                    address = addressDAO.save(address);
                log.info("Адресс с № помещения : " + address.getApartment() + " обновлён успешно");
                result.add(address);
            }
            messageResponse = result.isEmpty() ? messageResponse : "Обновлено " + result.size() + " адрессов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(comparators.sortedAddressById(result), List.of(messageResponse));
        } catch (
                Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAllAddress() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Получены все адресса";
        log.info(messageEnter(methodName));
        try {
            List<Address> result = addressDAO.findAll();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(comparators.sortedAddressById(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllAddress() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Получены все адресса";
        log.info(messageEnter(methodName));
        try {
            addressDAO.deleteAll();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // -------- street. house and number of apartment -----------------------
    @Override
    public Object getAddress(String street, String house, String numberApartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "По указанным параметрам адресс не существует";
        log.info(messageEnter(methodName));

        try {
            Address address = addressDAO.findByStreetAndHouseAndApartment(street, house, numberApartment);
            if (address != null) {
                messageResponse = "Адресс по указанным параметрам получен успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(address, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // получение общего адресса для всего дома --------------
    @Override
    public Object getAddressStart() {
        Address address = new Address("51931", "Украина", "Днепропетровская область",
                "Каменское", "Свободы", "51", "", "", "");
        try {
            return new Response(address, List.of("Стандартный адресс получен успешно"));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // sorted ------------------------------
    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
