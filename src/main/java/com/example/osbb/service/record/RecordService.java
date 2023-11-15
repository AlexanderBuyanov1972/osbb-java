package com.example.osbb.service.record;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dao.ShareDAO;
import com.example.osbb.dto.pojo.Client;
import com.example.osbb.dto.pojo.Room;
import com.example.osbb.dto.pojo.ListRoomAndListClient;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Record;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService implements IRecordService {
    private static final Logger log = Logger.getLogger(RecordService.class);
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OwnershipDAO ownershipDAO;
    @Autowired
    OwnerDAO ownerDAO;
    @Autowired
    ShareDAO shareDAO;

    @Override
    public Object createRecord(Record record) {
        log.info("Method createRecord : enter");
        List<String> errors = new ArrayList<>();
        try {
            if (record.getOwnership() == null) {
                String line = "В этой записи помещение не существует";
                log.info(line);
                errors.add(line);
            }
            if (record.getOwner() == null) {
                String line = "В этой записи собственник не существует";
                log.info(line);
                errors.add(line);
            }
            if (recordDAO.existsById(record.getId())) {
                String line = "Запись с ID : " + record.getId() + " уже существует";
                log.info(line);
                errors.add(line);
            }
            List<String> listErrors = List.of(
                    "Запись с таким номером помещения и с таким Ф.И.О. обственника уже существует.",
                    "Проверьте правильность заполнения данных.",
                    "Не может быть две записи одинаковых записи по номеру помещения и Ф.И.О. обственника одновременно."
            );
            if (recordDAO.existsByOwnershipIdAndOwnerId(record.getOwnership().getId(), record.getOwner().getId())) {
                listErrors.forEach(log::info);
                errors.addAll(listErrors);
            }
            if (errors.isEmpty()) {
                Owner owner = record.getOwner();
                owner.setActive(true);
                log.info("Собственник активирован");
                record.setOwner(owner);
                ownerDAO.save(owner);
                record.setCreateAt(LocalDateTime.now());
                log.info("Установлена дата создания записи");
                record = recordDAO.save(record);
                log.info("Запись создана успешно");
                log.info("Method createRecord : exit");
                return Response
                        .builder()
                        .data(record)
                        .messages(List.of("Запись создана успешно"))
                        .build();
            }
            log.info("Запись не создана по причине :");
            errors.forEach(log::info);
            log.info("Method createRecord : exit");
            return new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object updateRecord(Record record) {
        log.info("Method updateRecord : enter");
        List<String> errors = new ArrayList<>();
        try {
            if (record.getOwnership() == null) {
                String line = "В этой записи помещение не существует";
                log.info(line);
                errors.add(line);
            }
            if (record.getOwner() == null) {
                String line = "В этой записи собственник не существует";
                log.info(line);
                errors.add(line);
            }
            if (!recordDAO.existsById(record.getId())) {
                String line = "Запись с ID : " + record.getId() + " не существует";
                log.info(line);
                errors.add(line);
            }
            List<String> listErrors = List.of(
                    "Запись с таким номером помещения и с таким Ф.И.О. обственника отсутствует.",
                    "Проверьте правильность заполнения данных.",
                    "Невозможно обновить запись, которая не существует в базе данных."
            );
            if (!recordDAO.existsByOwnershipIdAndOwnerId(record.getOwnership().getId(), record.getOwner().getId())) {
                listErrors.forEach(log::info);
                errors.addAll(listErrors);
            }
            record.setUpdateAt(LocalDateTime.now());
            log.info("Обновлена дата создания записи");
            if (errors.isEmpty()) {
                record = recordDAO.save(record);
                log.info("Запись обновлена успешно");
                log.info("Method updateRecord : exit");
                return Response
                        .builder()
                        .data(record)
                        .messages(List.of("Запись обновлена успешно"))
                        .build();
            }
            log.info("Запись не обновлена по причине : ");
            errors.forEach(log::info);
            log.info("Method updateRecord : exit");
            return new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getRecord(Long id) {
        log.info("Method getRecord : enter");
        try {
            if (!recordDAO.existsById(id)) {
                log.info("Запись с ID : " + id + " не существует");
                log.info("Method getRecord : exit");
                return new ResponseMessages(List.of("Запись с ID : " + id + " не существует"));
            }
            Record record = recordDAO.findById(id).get();
            log.info("Запись получена успешно");
            log.info("Method getRecord : exit");
            return Response
                    .builder()
                    .data(record)
                    .messages(List.of("Запись получена успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object deleteRecord(Long id) {
        log.info("Method deleteRecord : enter");
        try {
            if (!recordDAO.existsById(id)) {
                log.info("Запись с ID : " + id + " не существует");
                log.info("Method deleteRecord : exit");
                return new ResponseMessages(List.of("Запись с ID : " + id + " не существует"));
            }
            recordDAO.deleteById(id);
            log.info("Запись с ID : " + id + " удалена успешно");
            log.info("Method deleteRecord : exit");
            return Response
                    .builder()
                    .data(id)
                    .messages(List.of("Запись с ID : " + id + " удалена успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object createAllRecord(List<Record> records) {
        log.info("Method createAllRecord : enter");
        List<Record> result = new ArrayList<>();
        try {
            for (Record record : records) {
                if (!recordDAO.existsById(record.getId())) {
                    record.setCreateAt(LocalDateTime.now());
                    record = recordDAO.save(record);
                    log.info("Запись с ID : " + record.getId() + " создана успешно");
                    result.add(record);
                }
            }
            if (result.isEmpty()) {
                log.info("Не создано ни одной записи");
                log.info("Method createAllRecord : exit");
                return new ResponseMessages(List.of("Не создано ни одной записи"));
            }
            log.info("Создано " + result.size() + " записей");
            log.info("Method createAllRecord : exit");
            return Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of("Создано " + result.size() + " записей"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object updateAllRecord(List<Record> records) {
        log.info("Method updateAllRecord : enter");
        List<Record> result = new ArrayList<>();
        try {
            for (Record record : records) {
                if (recordDAO.existsById(record.getId())) {
                    record.setUpdateAt(LocalDateTime.now());
                    record = recordDAO.save(record);
                    log.info("Запись с ID : " + record.getId() + " создана успешно");
                    result.add(record);
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлено ни одной записи");
                log.info("Method updateAllRecord : exit");
                return new ResponseMessages(List.of("Не обновлено ни одной записи"));
            }
            log.info("Обновлено " + result.size() + " записей");
            log.info("Method updateAllRecord : exit");
            return Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of("Обновлено " + result.size() + " записей"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAllRecord() {
        log.info("Method getAllRecord : enter");
        try {
            List<Record> result = recordDAO.findAll();
            log.info("Получено " + result.size() + " записей");
            log.info("Method getAllRecord : exit");
            return Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of("Получено " + result.size() + " записей"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object deleteAllRecord() {
        log.info("Method deleteAllRecord : enter");
        try {
            recordDAO.deleteAll();
            log.info("Все записи успешно удалены");
            log.info("Method deleteAllRecord : exit");
            return new ResponseMessages(List.of("Все записи успешно удалены"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getRoomsAndClientsByOwnershipId(Long id) {
        log.info("Method getRoomsAndClientsByOwnershipId : enter");
        try {
            List<Room> rooms = List.of(new Room(ownershipDAO.findById(id).orElse(new Ownership()), Double.parseDouble("0")));
            log.info("Помещения получены успешно");
            List<Client> clients = recordDAO.findAll()
                    .stream()
                    .filter(s -> s.getOwnership().getId() == id)
                    .map(s -> new Client(
                            s.getOwner(), shareDAO.findByOwnerIdAndOwnershipId(
                                    s.getOwner().getId(),
                                    s.getOwnership().getId())
                            .getValue()))
                    .toList();
            log.info("Клиенты получены успешно");
            log.info("Method getRoomsAndClientsByOwnershipId : exit");
            return Response
                    .builder()
                    .data(new ListRoomAndListClient(rooms, clients))
                    .messages(List.of("Клиенты получены успешно", "Помещения получены успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public Object getRoomsAndClientsByOwnerId(Long id) {
        log.info("Method getRoomsAndClientsByOwnerId : enter");
        try {
            List<Client> clients = List.of(new Client(ownerDAO.findById(id).orElse(new Owner()), 0.00));
            log.info("Клиенты получены успешно");
            List<Room> rooms = recordDAO.findAll()
                    .stream()
                    .filter(s -> s.getOwner().getId() == id)
                    .map(s -> new Room(
                            s.getOwnership(), Double.parseDouble("0")
                    ))
                    .toList();
            log.info("Помещения получены успешно");
            log.info("Method getRoomsAndClientsByOwnerId : учше");
            return Response
                    .builder()
                    .data(new ListRoomAndListClient(rooms, clients))
                    .messages(List.of("Клиенты получены успешно", "Помещения получены успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getRecordByApartmentAndFullName(String apartment, String fullName) {
        log.info("Method getRoomsAndClientsByOwnerId : enter");
        String[] fios = fullName.split(" ");
        String lineBase = "Записи с помещением № " + apartment + " и ФИО : " + fullName;
        String lineError = " не существует";
        String lineSuccessfully = " успешно получена";
        try {
            if (!recordDAO.existsByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                    apartment, fios[0], fios[1], fios[2])) {
                log.info(lineBase + lineError);
                log.info("Method getRoomsAndClientsByOwnerId : exit");
                return new ResponseMessages(List.of(lineBase + lineError));
            }
            Record record = recordDAO.findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                    apartment, fios[0], fios[1], fios[2]);
            log.info(lineBase + lineSuccessfully);
            log.info("Method getRoomsAndClientsByOwnerId : exit");
            return Response
                    .builder()
                    .data(record)
                    .messages(List.of(lineBase + lineSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object deleteRecordByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        log.info("Method deleteRecordByOwnerIdAndOwnershipId : enter");
        try {
            Record record = recordDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == ownerId)
                    .filter(s -> s.getOwnership().getId() == ownershipId)
                    .findFirst().orElse(null);
            String lineError = "По данным ID собственника : " + ownerId + " и ID помещения : " + ownershipId + " запись не существует";
            if (record == null) {
                log.info(lineError);
                log.info("Method deleteRecordByOwnerIdAndOwnershipId : exit");
                return Response
                        .builder()
                        .data(new Record())
                        .messages(List.of(lineError))
                        .build();
            }
            recordDAO.deleteById(record.getId());

            if (isRecordListEmptyByOwnerId(ownerId)) {
                Owner owner = ownerDAO.findById(ownerId).orElse(new Owner());
                owner.setActive(false);
                log.info("Деактивация собственника, по причине отсутствия долей с его участием");
                ownerDAO.save(owner);
            }
            log.info("Запись успешно удалена");
            log.info("Method deleteRecordByOwnerIdAndOwnershipId : exit");
            return Response
                    .builder()
                    .data(record.getId())
                    .messages(List.of("Запись успешно удалена"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    public boolean isRecordListEmptyByOwnerId(Long id) {
        return recordDAO.findAll()
                .stream()
                .filter(s -> s.getOwner().getId() == id)
                .toList().isEmpty();
    }

    // sorted ------------------------
    private List<Record> returnListSortedById(List<Record> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    boolean isEqualsRecordByFullname(Record s, String fullName) {
        return (s.getOwner().getLastName()
                + " " + s.getOwner().getFirstName()
                + " " + s.getOwner().getSecondName()).equals(fullName);
    }
}
