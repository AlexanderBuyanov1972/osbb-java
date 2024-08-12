package com.example.osbb.service.ownership;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.IdAndBill;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OwnershipService implements IOwnershipService {
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private RecordDAO recordDAO;
    @Autowired
    private OwnerDAO ownerDAO;

    // one --------------------------------------------
    @Override
    @Transactional
    public ResponseEntity<?> createOwnership(Ownership ownership) {
        String message = "Помещение с номером лицевого счёта " + ownership.getBill() + " уже существует";
        if (!ownershipDAO.existsByBill(ownership.getBill())) {
            ownership = ownershipDAO.save(ownership);
            message = "Помещение № " + ownership.getAddress().getApartment() + " создано успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(ownership, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOwnership(Ownership ownership) {
        String message = "Помещение с ID : " + ownership.getId() + " не существует";
        if (!ownershipDAO.existsById(ownership.getId())) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        if (!ownershipDAO.existsByAddressApartment(ownership.getAddress().getApartment())) {
            message = "Помещение с номером помещения " + ownership.getAddress().getApartment() + " не существует";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        if (!ownershipDAO.existsByBill(ownership.getBill())) {
            message = "Помещение с номером лицевого счёта " + ownership.getBill() + " не существует";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        ownership = ownershipDAO.save(ownership);
        message = "Помещение c ID : " + ownership.getId() + " обновлено успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(ownership, List.of(message)));
    }

    @Override
    public ResponseEntity<?> getOwnership(Long id) {
        String message = "Помещение с ID : " + id + " не существует";
        Ownership ownership = ownershipDAO.findById(id).orElse(null);
        if (ownership == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Помещение № " + ownership.getAddress().getApartment() + " получено успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(ownership, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteOwnership(Long id) {
        String message = "Собственность с ID : " + id + " не существует";
        if (ownershipDAO.existsById(id)) {
            ownershipDAO.deleteById(id);
            message = "Собственность удалена успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    // all -------------------------------------
    @Override
    @Transactional
    public ResponseEntity<?> createAllOwnership(List<Ownership> ownerships) {
        String message = "Не создано ни одного объекта собственности";
        List<Ownership> result = new ArrayList<>();
        for (Ownership one : ownerships) {
            if (!ownershipDAO.existsById(one.getId())) {
                ownershipDAO.save(one);
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " объектов собственности";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedOwnershipById(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllOwnership(List<Ownership> ownerships) {
        String message = "Не обновлено ни одного объекта собственности";
        List<Ownership> result = new ArrayList<>();
        for (Ownership one : ownerships) {
            if (ownershipDAO.existsById(one.getId())) {
                ownershipDAO.save(one);
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " помещений";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedOwnershipById(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllOwnership() {
        String message = "Не получено ни одного объекта собственности";
        List<Ownership> result = ownershipDAO.findAll().stream().sorted(comparatorByApartment()).toList();
        message = result.isEmpty() ? message : "Получено " + result.size() + " помещений";
        log.info(message);
        return ResponseEntity.ok(new Response(result, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllOwnership() {
        String message = "Все объекты собственности успешно удалены";
        ownershipDAO.deleteAll();
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    // summa ----------------------
    @Override
    public ResponseEntity<?> summaAreaRooms() {
        double summa = ownershipDAO.findAll().stream().mapToDouble(Ownership::getTotalArea).sum();
        String message = "Общая площадь помещений дома составляет " + summa + " м2.";
        log.info(message);
        return ResponseEntity.ok(new Response(summa, List.of(message)));
    }

    @Override
    public ResponseEntity<?> summaAreaApartment() {
        double summa = ownershipDAO.findAll().stream()
                .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                .mapToDouble(Ownership::getTotalArea)
                .sum();
        String message = "Общая площадь всех квартир дома составляет " + summa + " м2.";
        log.info(message);
        return ResponseEntity.ok(new Response(summa, List.of(message)));
    }

    @Override
    public ResponseEntity<?> summaAreaLivingApartment() {
        double summa = ownershipDAO.findAll().stream()
                .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                .mapToDouble(Ownership::getLivingArea).sum();
        String message = "Общая жилая площадь всех квартир дома составляет " + summa + " м2.";
        log.info(message);
        return ResponseEntity.ok(new Response(summa, List.of(message)));

    }

    @Override
    public ResponseEntity<?> summaAreaNonResidentialRoom() {
        double summa = ownershipDAO.findAll().stream()
                .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                .mapToDouble(Ownership::getTotalArea)
                .sum();
        String message = "Общая площадь всех нежилых помещений дома составляет " + summa + " м2.";
        log.info(message);
        return ResponseEntity.ok(new Response(summa, List.of(message)));
    }

    // count --------------------
    @Override
    public ResponseEntity<?> countRooms() {
        long count = ownershipDAO.count();
        String message = "Общее количество всех помещений дома составляет " + count + " единиц.";
        log.info(message);
        return ResponseEntity.ok(new Response(count, List.of(message)));

    }

    @Override
    public ResponseEntity<?> countApartment() {
        long count = ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT);
        String message = "Общее количество всех жилых помещений дома составляет " + count + " единиц.";
        log.info(message);
        return ResponseEntity.ok(new Response(count, List.of(message)));
    }

    @Override
    public ResponseEntity<?> countNonResidentialRoom() {
        long count = ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM);
        String message = "Общее количество всех нежилых помещений дома составляет " + count + " единиц.";
        log.info(message);
        return ResponseEntity.ok(new Response(count, List.of(message)));
    }

    // получить все помещения по номеру помещения --------------
    @Override
    public ResponseEntity<?> getAllOwnershipByApartment(String apartment) {
        String message = "Объекты недвижимости по помещению № : " + apartment + " не зарегистрированы";
        List<Ownership> ownerships = ownershipDAO.findByAddressApartment(apartment).stream().sorted(comparatorByBill())
                .toList();
        if (ownerships.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Объекты недвижимости с помещением № : " + apartment + " получено успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedOwnershipByBill(ownerships), List.of(message)));

    }

    //  получить все помещения по ФИО --------------------
    @Override
    public ResponseEntity<?> getAllApartmentByFullName(String fullName) {
        String message = "По данному Ф.И.О. : " + fullName + " собственник не найден";
        String[] fios = fullName.split(" ");
        Owner owner = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);
        if (owner == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        List<String> result = recordDAO.findAll().stream()
                .filter(s -> s.getOwner().getId() == owner.getId())
                .map(s -> "Помещение № " + s.getOwnership().getAddress().getApartment())
                .toList();
        if (result.isEmpty()) {
            owner.setActive(false);
            ownerDAO.save(owner);
            message = "По данному Ф.И.О. : " + fullName + " объектов недвижимости не найдено";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Получено " + result.size() + " записей";
        log.info(message);
        return ResponseEntity.ok(new Response(result, List.of(message)));
    }

    // получить помещение по лицевому счёту -------------
    @Override
    public ResponseEntity<?> getOwnershipByBill(String bill) {
        String message = "Помещение по лицевому счёту № : " + bill + " не существует";
        Ownership ownership = ownershipDAO.findByBill(bill);
        if (ownership == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(ownership, List.of(message)));
        }
        message = "Помещение по лицевому счёту № : " + bill + " успешно получено";
        log.info(message);
        return ResponseEntity.ok(new Response(ownership, List.of(message)));
    }

    @Override
    public ResponseEntity<?> getMapApartmentListIdAndBill() {
        Map<String, List<IdAndBill>> map = ownershipDAO.findAll().stream()
                .collect(Collectors.groupingBy(s -> s.getAddress().getApartment(),
                        Collectors.mapping(s -> new IdAndBill(s.getId(), s.getBill()), Collectors.toList())));
        log.info("Map<String, List<IdAndBill>> map = {}", map);
        return ResponseEntity.ok(new Response(map, List.of("Карта получена успешно")));

    }

    // получить все лицевые счета по номеру помещения ----------------
    @Override
    public ResponseEntity<?> getAllBillByApartment(String apartment) {
        String message = "По помещению № : " + apartment + " лицевые счёта не найдены";
        List<String> bills = ownershipDAO.findByAddressApartment(apartment).stream().map(Ownership::getBill).toList();
        if (bills.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "По помещению № : " + apartment + " найдено " + bills.size() + " лицевых счетов";
        log.info(message);
        return ResponseEntity.ok(new Response(bills, List.of(message)));


    }

    // sorted -----------------------------------------
    public List<Ownership> sortedOwnershipById(List<Ownership> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).toList();
    }

    public List<Ownership> sortedOwnershipByBill(List<Ownership> list) {
        return list.stream().sorted(comparatorByBill()).toList();
    }

    public Comparator<Ownership> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getAddress().getApartment())
                - Integer.parseInt(b.getAddress().getApartment());
    }

    public Comparator<Ownership> comparatorByBill() {
        return (a, b) -> a.getBill().compareTo(b.getBill());
    }



}
