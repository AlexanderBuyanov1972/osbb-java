package com.example.osbb.service.address;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.ownership.Address;
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
public class AddressService implements IAddressService {

    @Autowired
    private AddressDAO addressDAO;

    // ----------------- one -------------------------------
    @Override
    @Transactional
    public ResponseEntity<?> createAddress(Address address) {
        address = addressDAO.save(address);
        String message = "Адресс с ID : " + address.getId() + " создан успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(address, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAddress(Address address) {
        String message = "Адресс с ID : " + address.getId() + " не существует";
        if (addressDAO.existsById(address.getId())) {
            address = addressDAO.save(address);
            message = "Адресс c ID : " + address.getId() + " обновлён успешно";
            return ResponseEntity.ok(new Response(address, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAddress(Long id) {
        String message = "Адресс с ID : " + id + " не существует";
        Address address = addressDAO.findById(id).orElse(null);
        if (address != null) {
            message = "Адресс c ID : " + id + " получен успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(address, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAddress(Long id) {
        String message = "Адресс с ID : " + id + " не существует";
        if (addressDAO.existsById(id)) {
            addressDAO.deleteById(id);
            message = "Адресс c ID : " + id + " удалён успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));

    }

    // ---------------- all ----------------
    @Override
    @Transactional
    public ResponseEntity<?> createAllAddress(List<Address> addresses) {
        String message = "Не создано ни одного адресса";
        List<Address> result = new ArrayList<>();
        for (Address address : addresses) {
            if (!addressDAO.existsById(address.getId())) {
                address = addressDAO.save(address);
                log.info("Адресс с № помещения : {} создан успешно", address.getApartment());
                result.add(address);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " адрессов";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedAddressById(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllAddress(List<Address> addresses) {
        String message = "Не обновлено ни одного адресса";
        List<Address> result = new ArrayList<>();
        for (Address address : addresses) {
            if (addressDAO.existsById(address.getId()))
                address = addressDAO.save(address);
            log.info("Адресс с № помещения : {} обновлён успешно", address.getApartment());
            result.add(address);
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " адрессов";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedAddressById(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllAddress() {
        String message = "Получены все адресса";
        List<Address> result = addressDAO.findAll();
        log.info(message);
        return ResponseEntity.ok(new Response(sortedByApartment(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllAddress() {
        String message = "Получены все адресса";
        addressDAO.deleteAll();
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    // -------- street. house and number of apartment -----------------------
    @Override
    public ResponseEntity<?> getAddress(String street, String house, String numberApartment) {
        String message = "По указанным параметрам адресс не существует";
        Address address = addressDAO.findByStreetAndHouseAndApartment(street, house, numberApartment);
        if (address != null) {
            message = "Адресс по указанным параметрам получен успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(address, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(address, List.of(message)));
    }

    // получение общего адресса для всего дома --------------
    @Override
    public ResponseEntity<?> getAddressStart() {
        Address address = new Address("51931", "Украина", "Днепропетровская область",
                "Каменское", "Свободы", "51", "", "", "");
        return ResponseEntity.ok(new Response(address, List.of("Стандартный адресс получен успешно")));

    }

    // sorted --------------------------------------

    public List<Address> sortedAddressById(List<Address> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }


    public List<Address> sortedByApartment(List<Address> list) {
        return list.stream()
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .collect(Collectors.toList());
    }

}
