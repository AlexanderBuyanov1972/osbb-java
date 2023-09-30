package com.example.osbb.service.records;

import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.ErrorResponseMessages;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ResponseMessages;
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
            } else{
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

    // sorted ------------------------
    private List<Record> returnListSortedById(List<Record> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
