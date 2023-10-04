package com.example.osbb.service.records;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dao.ShareDAO;
import com.example.osbb.dto.pojo.Client;
import com.example.osbb.dto.pojo.Room;
import com.example.osbb.dto.pojo.ListRoomAndListClient;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;
import com.example.osbb.entity.Record;
import com.example.osbb.entity.Share;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService implements IRecordService {

    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OwnershipDAO ownershipDAO;
    @Autowired
    OwnerDAO ownerDAO;
    @Autowired
    ShareDAO shareDAO;

    @Override
//    @Transactional
    public Object createRecord(Record record) {
        List<String> list = new ArrayList<>();
        try {
            if (recordDAO.existsById(record.getId())) {
                list.add(ServiceMessages.ALREADY_EXISTS);
            }
            if (recordDAO.existsByOwnershipIdAndOwnerId(record.getOwnership().getId(), record.getOwner().getId())) {
                list.addAll(List.of(
                        "Запись с таким номером помещения и с таким Ф.И.О. обственника уже существует.",
                        "Проверьте правильность заполнения данных.",
                        "Не может быть две записи одинаковых записи по номеру помещения и Ф.И.О. обственника одновременно."
                ));
            }
            record.setCreateAt(LocalDateTime.now());
            return list.isEmpty() ? Response
                    .builder()
                    .data(recordDAO.save(record))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
//    @Transactional
    public Object updateRecord(Record record) {
        List<String> list = new ArrayList<>();
        try {
            if (!recordDAO.existsById(record.getId())) {
                list.add(ServiceMessages.NOT_EXISTS);
            }
            if (!recordDAO.existsByOwnershipIdAndOwnerId(record.getOwnership().getId(), record.getOwner().getId())) {
                list.addAll(List.of(
                        "Запись с таким номером помещения и с таким Ф.И.О. обственника отсутствует.",
                        "Проверьте правильность заполнения данных.",
                        "Невозможно обновить запись, которая не существует в базе данных."
                ));
            }
            record.setUpdateAt(LocalDateTime.now());

            return list.isEmpty() ? Response
                    .builder()
                    .data(recordDAO.save(record))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getRecord(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (!recordDAO.existsById(id)) {
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(recordDAO.findById(id).orElse(new Record()))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deleteRecord(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (!recordDAO.existsById(id)) {
                list.add(ServiceMessages.NOT_EXISTS);
                recordDAO.deleteById(id);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object createAllRecord(List<Record> records) {
        List<Record> result = new ArrayList<>();
        try {
            for (Record record : records) {
                if (!recordDAO.existsById(record.getId())) {
                    record.setCreateAt(LocalDateTime.now());
                    result.add(recordDAO.save(record));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object updateAllRecord(List<Record> records) {
        List<Record> result = new ArrayList<>();
        try {
            for (Record record : records) {
                if (recordDAO.existsById(record.getId())) {
                    record.setUpdateAt(LocalDateTime.now());
                    result.add(recordDAO.save(record));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getAllRecord() {
        try {
            List<Record> result = recordDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(returnListSortedById(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deleteAllRecord() {
        try {
            recordDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getRoomsAndClientsByOwnershipId(Long id) {
        try {
            List<Room> rooms = List.of(new Room(ownershipDAO.findById(id).orElse(new Ownership()), Double.parseDouble("0")));
            List<Client> clients = recordDAO.findAll()
                    .stream()
                    .filter(s -> s.getOwnership().getId() == id)
                    .map(s -> new Client(
                            s.getOwner(), Double.parseDouble("0")))
                    .toList();
            return Response
                    .builder()
                    .data(new ListRoomAndListClient(rooms, clients))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object getRoomsAndClientsByOwnerId(Long id) {
        try {
            List<Client> clients = List.of(new Client(ownerDAO.findById(id).orElse(new Owner()), 0.00));
            List<Room> rooms = recordDAO.findAll()
                    .stream()
                    .filter(s -> s.getOwner().getId() == id)
                    .map(s -> new Room(
                            s.getOwnership(), Double.parseDouble("0")
                    ))
                    .toList();
            return Response
                    .builder()
                    .data(new ListRoomAndListClient(rooms, clients))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getRecordByApartmentAndFullName(String apartment, String fullName) {
        List<String> errors = new ArrayList<>();
        String[] fios = fullName.split(" ");
        try {
            if (!recordDAO.existsByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                    apartment, fios[0], fios[1], fios[2])) {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(recordDAO.findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                            apartment, fios[0], fios[1], fios[2]
                    ))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deleteRecordByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        try {
            Record record = recordDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == ownerId)
                    .filter(s -> s.getOwnership().getId() == ownershipId)
                    .findFirst().orElse(null);
            if (record == null) {
                return Response
                        .builder()
                        .data(new Record())
                        .messages(List.of("По данным ID собственника и ID помещения запись не существует"))
                        .build();
            }
            recordDAO.deleteById(record.getId());

            if (isRecordListEmptyByOwnerId(ownerId)) {
                Owner owner = ownerDAO.findById(ownerId).get();
                // деактивация собственника, по причине отсутствия долей с его участием
                owner.setActive(false);
                ownerDAO.save(owner);
            }
            return Response
                    .builder()
                    .data(record.getId())
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
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
}
