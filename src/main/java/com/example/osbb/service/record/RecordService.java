package com.example.osbb.service.record;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Record;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService implements IRecordService {
    private static final Logger log = Logger.getLogger(IRecordService.class);

    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OwnershipDAO ownershipDAO;
    @Autowired
    OwnerDAO ownerDAO;

    @Override
    public Object createRecord(Record record) {
        String methodName = "createRecord";
        String messageOwnershipNotExists = "В этой записи объект помещение не существует";
        String messageOwnerNotExists = "В этой записи объект собственник не существует";
        String messageRecordAlreadyExists = "Запись с такими номером помещения и ФИО уже существует";

        log.info(messageEnter(methodName));
        try {
            if (record.getOwnership() == null) {
                log.error(messageOwnershipNotExists);
                return new ErrorResponseMessages(List.of(messageOwnershipNotExists));
            }
            if (record.getOwner() == null) {
                log.error(messageOwnerNotExists);
                return new ErrorResponseMessages(List.of(messageOwnerNotExists));
            }
            if (recordDAO.existsByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                    record.getOwnership().getAddress().getApartment(),
                    record.getOwner().getLastName(),
                    record.getOwner().getFirstName(),
                    record.getOwner().getSecondName())) {
                log.error(messageRecordAlreadyExists);
                return new ErrorResponseMessages(List.of(messageRecordAlreadyExists));
            }

            Ownership ownership = ownershipDAO.save(record.getOwnership());
            log.info("Собственность с ID : " + ownership.getId() + " создана успешно");
            record.setOwnership(ownership);

            Owner owner = ownerDAO.save(record.getOwner());
            log.info("Собственник с ID : " + owner.getId() + " создан успешно");
            owner.setActive(true);
            log.info("Собственник активирован успешно");
            record.setOwner(owner);


            record.setCreateAt(LocalDateTime.now());
            log.info("Установлена дата создания записи");

            record = recordDAO.save(record);
            String messageSuccessfully = "Запись с ID : " + record.getId() + " создана успешно";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(record)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Transactional
    @Override
    public Object updateRecord(Record record) {
        String methodName = "updateRecord";
        String messageOwnershipNotExists = "В этой записи объект помещение не существует";
        String messageOwnerNotExists = "В этой записи объект собственник не существует";
        String messageRecordNotExists = "Запись с ID : " + record.getId() + " не существует";
        String messageSuccessfully = "Запись обновлена успешно";
        List<String> listErrors = List.of(
                "Запись с таким номером помещения и с таким Ф.И.О. обственника отсутствует.",
                "Проверьте правильность заполнения данных.",
                "Невозможно обновить запись, которая не существует в базе данных."
        );

        log.info(messageEnter(methodName));
        List<String> errors = new ArrayList<>();
        try {
            if (record.getOwnership() == null) {
                log.info(messageOwnershipNotExists);
                return new ErrorResponseMessages(List.of(messageOwnershipNotExists));
            }
            if (record.getOwner() == null) {
                log.info(messageOwnerNotExists);
                return new ErrorResponseMessages(List.of(messageOwnerNotExists));
            }
            if (!recordDAO.existsById(record.getId())) {
                log.info(messageRecordNotExists);
                return new ErrorResponseMessages(List.of(messageRecordNotExists));
            }

            if (!recordDAO.existsByOwnershipIdAndOwnerId(record.getOwnership().getId(), record.getOwner().getId())) {
                listErrors.forEach(log::info);
                return new ErrorResponseMessages(listErrors);
            }
            record.setUpdateAt(LocalDateTime.now());
            log.info(messageSuccessfully);

            record = recordDAO.save(record);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(record)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getRecord(Long id) {
        String methodName = "getRecord";
        String messageNotExists = "Запись с ID : " + id + " не существует";
        String messageSuccessfully = "Запись с ID : " + id + " получена успешно";
        String messageResponse = "";

        log.info(messageEnter(methodName));
        try {
            Record record = recordDAO.findById(id).orElse(null);
            messageResponse = record == null ? messageNotExists : messageSuccessfully;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(record)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Transactional
    @Override
    public Object deleteRecord(Long id) {
        String methodName = "deleteRecord";
        String messageNotExists = "Запись с ID : " + id + " не существует";
        String messageSuccessfully = "Запись с ID : " + id + " удалена успешно";

        log.info(messageEnter(methodName));
        try {
            if (!recordDAO.existsById(id)) {
                log.info(messageNotExists);
                log.info(messageExit(methodName));
                return new ResponseMessages(List.of(messageNotExists));
            }
            recordDAO.deleteById(id);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(id)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Transactional
    @Override
    public Object createAllRecord(List<Record> records) {
        String methodName = "createAllRecord";
        String messageNotCreate = "Не создано ни одной записи";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
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
            messageSuccessfully = result.isEmpty() ? messageNotCreate : "Создано " + result.size() + " записей";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Transactional
    @Override
    public Object updateAllRecord(List<Record> records) {
        String methodName = "updateAllRecord";
        String messageNotUpdate = "Не обновлено ни одной записи";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        List<Record> result = new ArrayList<>();
        try {
            for (Record record : records) {
                if (recordDAO.existsById(record.getId())) {
                    record.setUpdateAt(LocalDateTime.now());
                    record = recordDAO.save(record);
                    log.info("Запись с ID : " + record.getId() + " обновлена успешно");
                    result.add(record);
                }
            }
            messageSuccessfully = result.isEmpty() ? messageNotUpdate : "Обновлено " + result.size() + " записей";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllRecord() {
        String methodName = "getAllRecord";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            List<Record> result = recordDAO.findAll();
            messageSuccessfully = "Получено " + result.size() + " записей";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Transactional
    @Override
    public Object deleteAllRecord() {
        String methodName = "deleteAllRecord";
        String messageSuccessfully = "Все записи успешно удалены";

        log.info(messageEnter(methodName));
        try {
            recordDAO.deleteAll();
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new ResponseMessages(List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllRecordByOwnershipId(Long id) {
        String methodName = "getAllRecordByOwnershipId";
        String messageSuccessfully = "Записи по объекту недвижимости с ID : " + id + " получены успешно";
        log.info(messageEnter(methodName));
        try {
            List<Record> records = recordDAO.findByOwnershipId(id);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(records)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAllRecordByOwnerId(Long id) {
        String methodName = "getAllRecordByOwnerId";
        String messageSuccessfully = "Записи по собственнику с ID : " + id + " получены успешно";

        log.info(messageEnter(methodName));
        try {
            List<Record> records = recordDAO.findByOwnerId(id);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(records)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getRecordByApartmentAndFullName(String apartment, String fullName) {
        String methodName = "getRecordByApartmentAndFullName";
        String messageSuccessfully = "Запись с помещением № " + apartment + " и ФИО : " + fullName + " успешно получена";
        String messageNotExists = "Запись с помещением № " + apartment + " и ФИО : " + fullName + " не найдена";

        log.info(messageEnter(methodName));
        String[] fios = fullName.split(" ");
        try {
            Record record = recordDAO.findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                    apartment, fios[0], fios[1], fios[2]);
            String messageResponse = record == null ? messageNotExists : messageSuccessfully;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(record)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object deleteRecordByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        String methodName = "deleteRecordByOwnerIdAndOwnershipId";
        String messageNotExists = "По данным ID собственника : " + ownerId
                + " и ID помещения : " + ownershipId + " запись не существует";
        String messageDeleteSuccessfully = "Запись успешно удалена";
        String messageDeactivated = "Собственник деактивирован по причине отсутствия долей с его участием";
        log.info(messageEnter(methodName));
        try {
            Record record = recordDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == ownerId)
                    .filter(s -> s.getOwnership().getId() == ownershipId)
                    .findFirst().orElse(null);
            if (record == null) {
                log.info(messageNotExists);
                log.info(messageExit(methodName));
                return Response
                        .builder()
                        .data(new Record())
                        .messages(List.of(messageNotExists))
                        .build();
            }
            recordDAO.deleteById(record.getId());
            if (isRecordListEmptyByOwnerId(ownerId)) {
                Owner owner = ownerDAO.findById(ownerId).orElse(new Owner());
                owner.setActive(false);
                log.info(messageDeactivated);
                ownerDAO.save(owner);
            }
            log.info(messageDeleteSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(record.getId())
                    .messages(List.of(messageDeleteSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
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

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
