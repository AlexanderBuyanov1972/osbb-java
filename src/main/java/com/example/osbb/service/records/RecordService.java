package com.example.osbb.service.records;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.pojo.Client;
import com.example.osbb.dto.pojo.Room;
import com.example.osbb.dto.pojo.ListRoomAndListClient;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;
import com.example.osbb.entity.Record;
import com.example.osbb.service.ServiceMessages;
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

    @Override
    public Object createRecord(Record record) {
        List<String> list = new ArrayList<>();
        try {
            if (recordDAO.existsById(record.getId())) {
                list.add(ServiceMessages.ALREADY_EXISTS);
            } else {
                record.setCreateAt(LocalDateTime.now());
            }
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
    public Object updateRecord(Record record) {
        List<String> list = new ArrayList<>();
        try {
            if (!recordDAO.existsById(record.getId())) {
                list.add(ServiceMessages.NOT_EXISTS);
            } else {
                record.setUpdateAt(LocalDateTime.now());
            }
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
                    .data(recordDAO.findById(id).get())
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
            List<Room> rooms = List.of(new Room(ownershipDAO.findById(id).get()));
            List<Client> clients = recordDAO.findAll()
                    .stream()
                    .filter(s -> s.getOwnership().getId() == id)
                    .map(s -> new Client(s.getOwner()))
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
            List<Client> clients = List.of(new Client(ownerDAO.findById(id).get()));
            List<Room> rooms = recordDAO.findAll()
                    .stream()
                    .filter(s -> s.getOwner().getId() == id)
                    .map(s -> new Room(s.getOwnership()))
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
                            apartment,  fios[0], fios[1], fios[2]
                    ))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // sorted ------------------------
    private List<Record> returnListSortedById(List<Record> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
