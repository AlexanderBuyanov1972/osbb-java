package com.example.osbb.service.ownership;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.dto.pojo.Room;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnershipService implements IOwnershipService {
    private static final Logger log = Logger.getLogger(IOwnershipService.class);
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private RecordDAO recordDAO;
    @Autowired
    private OwnerDAO ownerDAO;

    // one --------------------------------------------

    @Override
    @Transactional
    public Object createOwnership(Ownership ownership) {
        log.info("Method createOwnership - enter");
        try {
            if (ownershipDAO.existsByAddressApartment(ownership.getAddress().getApartment())) {
                log.info("Помещение с номером помещения "
                        + ownership.getAddress().getApartment()
                        + " уже существует");
                log.info("Method createOwnership - exit");
                return new ResponseMessages(List.of("Помещение с номером помещения "
                        + ownership.getAddress().getApartment()
                        + " уже существует"));
            }
            ownership = ownershipDAO.save(ownership);
            log.info("Собственность создана успешно");
            log.info("Method createOwnership - exit");
            return Response
                    .builder()
                    .data(ownership)
                    .messages(List.of("Собственность создана успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwnership(Ownership ownership) {
        log.info("Method updateOwnership - enter");
        try {
            List<String> errors = new ArrayList<>();
            if (!ownershipDAO.existsById(ownership.getId())) {
                log.info("Собственность с ID : " + ownership.getId() + " не существует");
                errors.add("Собственность с ID : " + ownership.getId() + " не существует");
            }

            if (!ownershipDAO.existsByAddressApartment(ownership.getAddress().getApartment())) {
                log.info("Помещение с номером помещения "
                        + ownership.getAddress().getApartment()
                        + " не существует");
                errors.add("Помещение с номером помещения "
                        + ownership.getAddress().getApartment()
                        + " не существует");
            }
            if (!errors.isEmpty()) {
                log.info("Method updateOwnership - exit");
                return new ResponseMessages(errors);
            }
            ownership = ownershipDAO.save(ownership);
            log.info("Собственность обновлена успешно");
            log.info("Method updateOwnership - exit");
            return Response
                    .builder()
                    .data(ownership)
                    .messages(List.of("Собственность обновлена успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getOwnership(Long id) {
        log.info("Method getOwnership - enter");
        try {
            if (!ownershipDAO.existsById(id)) {
                log.info("Собственность с ID : " + id + " не существует");
                log.info("Method getOwnership - exit");
                return new ResponseMessages(List.of("Собственность с ID : " + id + " не существует"));
            }
            Ownership ownership = ownershipDAO.findById(id).get();
            log.info("Собственность с ID : " + ownership.getId() + " получена успешно");
            log.info("Method getOwnership - exit");
            return Response
                    .builder()
                    .data(ownership)
                    .messages(List.of("Собственность с ID : " + ownership.getId() + " получена успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getOwnershipByApartment(String apartment) {
        log.info("Method getOwnershipByApartment - enter");
        try {
            Ownership ownership = ownershipDAO.findByAddressApartment(apartment);
            if (ownership == null) {
                log.info("Собственность с номером квартиры : " + apartment + " не существует");
                log.info("Method getOwnershipByApartment - exit");
                return new ResponseMessages(List.of("Собственность с ID : " + apartment + " не существует"));
            }
            log.info("Собственность по помещению № : " + apartment + " получена успешно");
            log.info("Method getOwnershipByApartment - exit");
            return Response
                    .builder()
                    .data(ownership)
                    .messages(List.of("Собственность по помещению № : " + apartment + " получена успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getBillByApartment(String apartment) {
        log.info("Method getBillByApartment - enter");
        try {
            String bill = ownershipDAO.findByAddressApartment(apartment).getBill();
            if (bill == null || bill.isEmpty()) {
                log.info("По номеру помещения, равному " + apartment + " лицевой счёт не найден");
                log.info("Method getBillByApartment - exit");
                return new ResponseMessages(List.of("По номеру помещения равному " + apartment + " лицевой счёт не найден"));
            }
            log.info("Лицевой счёт № " + bill + " получен по помещению № " + apartment);
            log.info("Method getBillByApartment - exit");
            return Response
                    .builder()
                    .data(bill)
                    .messages(List.of("Лицевой счёт № " + bill + " получен по помещению № " + apartment))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }


    @Override
    @Transactional
    public Object deleteOwnership(Long id) {
        log.info("Method deleteOwnership - enter");
        try {
            if (ownershipDAO.existsById(id)) {
                ownershipDAO.deleteById(id);
                log.info("Собственность удалена успешно");
                log.info("Method deleteOwnership - exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Собственность удалена успешно"))
                        .build();
            }
            log.info("Собственность с ID : " + id + " не существует");
            log.info("Method deleteOwnership - exit");
            return new ResponseMessages(List.of("Собственность с ID : " + id + " не существует"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // all -------------------------------------

    @Override
    @Transactional
    public Object createAllOwnership(List<Ownership> ownerships) {
        log.info("Method createAllOwnership - enter");
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (!ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            if (result.isEmpty()) {
                log.info("Не создана ни одна собственность");
                log.info("Method createAllOwnership - exit");
                return new ResponseMessages(List.of("Не создано ни одного помещения"));

            }
            log.info("Создано " + result.size() + " помещений");
            log.info("Method createAllOwnership - exit");
            return Response
                    .builder()
                    .data(sortedById(result))
                    .messages(List.of("Создано " + result.size() + " помещений"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllOwnership(List<Ownership> ownerships) {
        log.info("Method updateAllOwnership - enter");
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлено ни одно помещение");
                log.info("Method updateAllOwnership - exit");
                return new ResponseMessages(List.of("Не обновлено ни одно помещение"));
            }
            log.info("Обновлено " + result.size() + " помещений");
            log.info("Method updateAllOwnership - exit");
            return Response
                    .builder()
                    .data(sortedById(result))
                    .messages(List.of("Обновлено " + result.size() + " помещений"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAllOwnership() {
        log.info("Method getAllOwnership - enter");
        try {
            List<Room> result = ownershipDAO.findAll().stream()
                    .map(s -> new Room(s, 0.00))
                    .sorted(comparatorRoomByApartment())
                    .toList();
            log.info("Получено " + result.size() + " помещений");
            log.info("Method getAllOwnership - exit");
            return Response
                    .builder()
                    .data(result)
                    .messages(List.of("Получено " + result.size() + " помещений"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwnership() {
        log.info("Method deleteAllOwnership - enter");
        try {
            ownershipDAO.deleteAll();
            log.info("Все помещения удалены успешно");
            log.info("Method deleteAllOwnership - exit");
            return new ResponseMessages(List.of("Все помещения удалены успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // summa ----------------------

    @Override
    public Object summaAreaRooms() {
        log.info("Method summaAreaRooms - enter");
        try {
            double summa = ownershipDAO.findAll().stream()
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            log.info("Общая площадь помещений дома составляет " + summa + " м2.");
            log.info("Method summaAreaRooms - exit");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая площадь помещений дома составляет " + summa + " м2."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object summaAreaApartment() {
        log.info("Method summaAreaApartment - enter");
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            log.info("Общая площадь всех квартир дома составляет " + summa + " м2.");
            log.info("Method summaAreaApartment - exit");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая площадь всех квартир дома составляет " + summa + " м2."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object summaAreaLivingApartment() {
        log.info("Method summaAreaLivingApartment - enter");
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getLivingArea)
                    .sum();
            log.info("Общая жилая площадь всех квартир дома составляет " + summa + " м2.");
            log.info("Method summaAreaLivingApartment - exit");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая жилая площадь всех квартир дома составляет " + summa + " м2."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object summaAreaNonResidentialRoom() {
        log.info("Method summaAreaNonResidentialRoom - enter");
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            log.info("Общая площадь всех нежилых помещений дома составляет " + summa + " м2.");
            log.info("Method summaAreaNonResidentialRoom - enter");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая площадь всех нежилых помещений дома составляет " + summa + " м2."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    // count --------------------

    @Override
    public Object countRooms() {
        log.info("Method countRooms - enter");
        try {
            long count = ownershipDAO.count();
            log.info("Общее количество всех помещений дома составляет " + count + " единиц.");
            log.info("Method countRooms - exit");
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество всех помещений дома составляет " + count + " единиц."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object countApartment() {
        log.info("Method countApartment - enter");
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT);
            log.info("Общее количество всех жилых помещений дома составляет " + count + " единиц.");
            log.info("Method countApartment - exit");
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество всех жилых помещений дома составляет " + count + " единиц."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object countNonResidentialRoom() {
        log.info("Method countNonResidentialRoom - enter");
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM);
            log.info("Общее количество всех нежилых помещений дома составляет " + count + " единиц.");
            log.info("Method countNonResidentialRoom - exit");
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество всех нежилых помещений дома составляет " + count + " единиц."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    // room --------------------------------
    @Override
    public Object getRoomByApartment(String apartment) {
        log.info("Method getRoomByApartment - enter");
        try {
            Ownership ownership = ownershipDAO.findByAddressApartment(apartment);
            if (ownership == null) {
                log.info("По номеру помещения " + apartment + " само помещение не найдено");
                log.info("Method getRoomByApartment - exit");
                return new ResponseMessages(List.of("По номеру помещения " + apartment + " само помещение не найдено"));
            }
            log.info("Помещение получено успешно");
            log.info("Method getRoomByApartment - exit");
            return Response
                    .builder()
                    .data(new Room(ownership, 0.00))
                    .messages(List.of("Помещение получено успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getListApartmentsByFullName(String fullName) {
        log.info("Method getListApartmentsByFullName - enter");
        try {
            String[] fios = fullName.split(" ");
            Owner owner = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);
            if (owner == null) {
                log.info("По данному ФИО : " + fullName + " собственник не найден");
                log.info("Method getListApartmentsByFullName - exit");
                return new ResponseMessages(List.of("По данному ФИО : " + fullName + " собственник не найден"));
            }
            List<String> result = recordDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == owner.getId())
                    .map(s -> "Квартира № " + s.getOwnership().getAddress().getApartment())
                    .collect(Collectors.toList());
            if (result.isEmpty()) {
                owner.setActive(false);
                ownerDAO.save(owner);
                log.info("По данному собственнику не зарегистрировано ни одно помещение");
                log.info("Method getListApartmentsByFullName - exit");
                return new ResponseMessages(List.of("По данному собственнику не зарегистрировано ни одно помещение"));
            }
            log.info("Получено " + result.size() + " записей");
            log.info("Method getListApartmentsByFullName - exit");
            return Response
                    .builder()
                    .data(result)
                    .messages(List.of("Получено " + result.size() + " записей"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // sorted -----------------------------------
    private List<Ownership> sortedById(List<Ownership> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    //.sorted(comparatorByApartment())
    private Comparator<Room> comparatorRoomByApartment() {
        return (a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment());
    }

    private Comparator<Ownership> comparatorOwnershipByApartment() {
        return (a, b) -> Integer.parseInt(a.getAddress().getApartment())
                - Integer.parseInt(b.getAddress().getApartment());
    }
}
