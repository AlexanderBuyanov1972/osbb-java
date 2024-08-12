package com.example.osbb.service.ownership;

import com.example.osbb.entity.ownership.Ownership;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOwnershipService {

    // ------------ one -----------------------------

    ResponseEntity<?> createOwnership(Ownership one);

    ResponseEntity<?> updateOwnership(Ownership one);

    ResponseEntity<?> getOwnership(Long id);

    ResponseEntity<?> deleteOwnership(Long id);

    // -------------- all -------------------

    ResponseEntity<?> createAllOwnership(List<Ownership> list);

    ResponseEntity<?> updateAllOwnership(List<Ownership> list);

    ResponseEntity<?> getAllOwnership();

    ResponseEntity<?> deleteAllOwnership();

    // summa area --------------------------------

    // Общая площадь жилых и нежилых помещений
    ResponseEntity<?> summaAreaRooms();

    // Площадь квартир
    ResponseEntity<?> summaAreaApartment();

    // Площадь жилая квартир
    ResponseEntity<?> summaAreaLivingApartment();

    // Площадь нежилых помещений
    ResponseEntity<?> summaAreaNonResidentialRoom();

    // count rooms ------------------------------------

    ResponseEntity<?> countRooms();

    // Количество квартир
    ResponseEntity<?> countApartment();

    // Количество нежилых помещений
    ResponseEntity<?> countNonResidentialRoom();

    // разное -----------------------------
    // получить все объекты собственности по номеру помещения (квартиры)
    ResponseEntity<?> getAllOwnershipByApartment(String apartment);

    // получить все лицевые счета по номеру помещения (квартиры)
    ResponseEntity<?> getAllBillByApartment(String apartment);

    // получить все номера квартир по ФИО -------------
    ResponseEntity<?> getAllApartmentByFullName(String fullName);

    // получить помещение по лицевому счёту -------------
    ResponseEntity<?> getOwnershipByBill(String bill);

    ResponseEntity<?> getMapApartmentListIdAndBill();


}
