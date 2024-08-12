package com.example.osbb.service.record;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Record;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecordService implements IRecordService {

    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OwnershipDAO ownershipDAO;
    @Autowired
    OwnerDAO ownerDAO;

    @Override
    public ResponseEntity<?> createRecord(Record record) {
        String message = "В этой записи объект помещение не существует";
        if (record.getOwnership() == null) {
            log.error(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        if (record.getOwner() == null) {
            message = "В этой записи объект собственник не существует";
            log.error(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        if (recordDAO.existsByOwnershipBillAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                record.getOwnership().getBill(),
                record.getOwner().getLastName(),
                record.getOwner().getFirstName(),
                record.getOwner().getSecondName())) {
            message = "Запись с лицевым счётом № : " + record.getOwnership().getBill()
                    + " и ФИО : " + mapOwnerToFullName(record.getOwner()) + " уже существует";
            log.error(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }

        Ownership ownership = ownershipDAO.save(record.getOwnership());
        log.info("Собственность с ID : {} создана успешно", ownership.getId());
        record.setOwnership(ownership);

        Owner owner = ownerDAO.save(record.getOwner());
        log.info("Собственник с ID : {} создан успешно", owner.getId());
        owner.setActive(true);
        log.info("Собственник активирован успешно");
        record.setOwner(owner);

        record.setCreateAt(LocalDateTime.now());
        log.info("Установлена дата создания записи");

        record = recordDAO.save(record);
        message = "Зарегистрирована собственность в помещение № : " +
                record.getOwnership().getAddress().getApartment() + " на собственника ФИО : " +
                mapOwnerToFullName(record.getOwner()) + " в размере доли : " + record.getShare() +
                " на дату : " + mapLocalDateTime(record.getCreateAt());
        log.info(message);
        return ResponseEntity.ok(new Response(record, List.of(message)));
    }


    @Transactional
    @Override
    public ResponseEntity<?> updateRecord(Record record) {
        String message = "В этой записи объект помещение не существует";
        List<String> listErrors = List.of(
                "Запись с таким номером помещения и с таким Ф.И.О. обственника отсутствует.",
                "Проверьте правильность заполнения данных.",
                "Невозможно обновить запись, которая не существует в базе данных."
        );
        if (record.getOwnership() == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        if (record.getOwner() == null) {
            message = "В этой записи объект собственник не существует";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        if (!recordDAO.existsById(record.getId())) {
            message = "Запись с ID : " + record.getId() + " не существует";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        if (!recordDAO.existsByOwnershipIdAndOwnerId(record.getOwnership().getId(), record.getOwner().getId())) {
            listErrors.forEach(log::info);
            return ResponseEntity.badRequest().body(new Response(listErrors));
        }
        record.setUpdateAt(LocalDateTime.now());
        record = recordDAO.save(record);
        message = "Запись обновлена успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(record, List.of(message)));
    }

    @Override
    public ResponseEntity<?> getRecord(Long id) {
        String message = "Запись с ID : " + id + " не существует";
        Record record = recordDAO.findById(id).orElse(null);
        if (record == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Запись с ID : " + id + " получена успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(record, List.of(message)));
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteRecord(Long id) {
        String message = "Запись с ID : " + id + " не существует";
        if (recordDAO.existsById(id)) {
            recordDAO.deleteById(id);
            message = "Запись с ID : " + id + " удалена успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Transactional
    @Override
    public ResponseEntity<?> createAllRecord(List<Record> records) {
        String message = "Не создано ни одной записи";
        List<Record> result = new ArrayList<>();
        for (Record record : records) {
            if (!recordDAO.existsById(record.getId())) {
                record.setCreateAt(LocalDateTime.now());
                record = recordDAO.save(record);
                log.info("Запись с ID : " + record.getId() + " создана успешно");
                result.add(record);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " записей";
        log.info(message);
        return ResponseEntity.ok(new Response(returnListSortedById(result), List.of(message)));
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateAllRecord(List<Record> records) {
        String message = "Не обновлено ни одной записи";
        List<Record> result = new ArrayList<>();
        for (Record record : records) {
            if (recordDAO.existsById(record.getId())) {
                record.setUpdateAt(LocalDateTime.now());
                record = recordDAO.save(record);
                log.info("Запись с ID : " + record.getId() + " обновлена успешно");
                result.add(record);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " записей";
        log.info(message);
        return ResponseEntity.ok(new Response(returnListSortedById(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllRecord() {
        List<Record> result = recordDAO.findAll();
        String message = "Получено " + result.size() + " записей";
        log.info(message);
        return ResponseEntity.ok(new Response(returnListSortedById(result), List.of(message)));
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteAllRecord() {
        String message = "Все записи успешно удалены";
        recordDAO.deleteAll();
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllRecordByOwnershipId(Long id) {
        List<Record> records = recordDAO.findByOwnershipId(id);
        String message = "Записи по объекту недвижимости с ID : " + id +
                " получены успешно в количестве " + records.size() + " штук";
        log.info(message);
        return ResponseEntity.ok(new Response(records, List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllRecordByOwnerId(Long id) {
        List<Record> records = recordDAO.findByOwnerId(id);
        String message = "Записи по собственнику с ID : " + id
                + " получены успешно в количестве " + records.size() + " штук";
        log.info(message);
        return ResponseEntity.ok(new Response(records, List.of(message)));
    }

    @Override
    public ResponseEntity<?> getRecordByApartmentAndFullName(String apartment, String fullName) {
        String message = "Запись с помещением № " + apartment + " и ФИО : " + fullName + " не найдена";
        String[] fios = fullName.split(" ");
        Record record = recordDAO.findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                apartment, fios[0], fios[1], fios[2]);
        if (record == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Запись с помещением № " + apartment + " и ФИО : " + fullName + " успешно получена";
        log.info(message);
        return ResponseEntity.ok(new Response(record, List.of(message)));
    }

    @Override
    public ResponseEntity<?> deleteRecordByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        String message = "По данным ID собственника : " + ownerId
                + " и ID помещения : " + ownershipId + " запись не существует";
        Record record = recordDAO.findAll().stream()
                .filter(s -> s.getOwner().getId() == ownerId)
                .filter(s -> s.getOwnership().getId() == ownershipId)
                .findFirst().orElse(null);
        if (record == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        recordDAO.deleteById(record.getId());
        if (isRecordListEmptyByOwnerId(ownerId)) {
            Owner owner = ownerDAO.findById(ownerId).orElse(new Owner());
            owner.setActive(false);
            log.info("Собственник деактивирован по причине отсутствия долей с его участием");
            ownerDAO.save(owner);
        }
        message = "Запись с ID : " + record.getId() + " успешно удалена";
        log.info(message);
        return ResponseEntity.ok(new Response(record.getId(), List.of(message)));

    }

    private String mapLocalDateTime(LocalDateTime date) {
        String line = date.toString().replace("T", " время : ");
        return line.substring(0, line.indexOf("."));
    }

    private boolean isRecordListEmptyByOwnerId(Long id) {
        return recordDAO.findAll().stream().filter(s -> s.getOwner().getId() == id).toList().isEmpty();
    }

    private boolean isEqualsRecordByFullname(Record s, String fullName) {
        return (s.getOwner().getLastName()
                + " " + s.getOwner().getFirstName()
                + " " + s.getOwner().getSecondName()).equals(fullName);
    }

    // sorted ------------------------
    private List<Record> returnListSortedById(List<Record> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private String mapOwnerToFullName(Owner o) {
        return o.getLastName() + " " + o.getFirstName() + " " + o.getSecondName();
    }

    public Comparator<List<Record>> comparatorRecordByApartment() {
        return (a, b) -> Integer.parseInt(a.get(0).getOwnership().getAddress().getApartment())
                - Integer.parseInt(b.get(0).getOwnership().getAddress().getApartment());
    }


}
