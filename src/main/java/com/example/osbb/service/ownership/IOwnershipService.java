package com.example.osbb.service.ownership;

import com.example.osbb.entity.ownership.Ownership;

import java.util.List;

public interface IOwnershipService {

    // ------------ one -----------------------------

    public Object createOwnership(Ownership one);

    public Object updateOwnership(Ownership one);

    public Object getOwnership(Long id);

    public Object deleteOwnership(Long id);

    // -------------- all -------------------

    public Object createAllOwnership(List<Ownership> list);

    public Object updateAllOwnership(List<Ownership> list);

    public Object getAllOwnership();

    public Object deleteAllOwnership();

    // summa area --------------------------------

    // Общая площадь жилых и нежилых помещений
    public Object summaAreaRooms();

    // Площадь квартир
    public Object summaAreaApartment();

    // Площадь жилая квартир
    public Object summaAreaLivingApartment();

    // Площадь нежилых помещений
    public Object summaAreaNonResidentialRoom();

    // count rooms ------------------------------------

    public Object countRooms();

    // Количество квартир
    public Object countApartment();

    // Количество нежилых помещений
    public Object countNonResidentialRoom();

    // разное -----------------------------
    // получить все объекты собственности по номеру помещения (квартиры)
    public Object getAllOwnershipByApartment(String apartment);

    // получить все лицевые счета по номеру помещения (квартиры)
    public Object getAllBillByApartment(String apartment);

    // получить все номера квартир по ФИО -------------
    public Object getAllApartmentByFullName(String fullName);

    // получить помещение по лицевому счёту -------------
    public Object getOwnershipByBill(String bill);

    public Object getMapApartmentListIdAndBill();


}
