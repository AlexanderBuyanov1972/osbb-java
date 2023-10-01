package com.example.osbb.service.records;

import com.example.osbb.entity.Record;

import java.util.List;

public interface IRecordService {
    //  one -----------------------------
    public Object createRecord(Record record);

    public Object updateRecord(Record record);

    public Object getRecord(Long id);

    public Object deleteRecord(Long id);

    // all ----------------------------

    public Object createAllRecord(List<Record> records);

    public Object updateAllRecord(List<Record> records);

    public Object getAllRecord();

    public Object deleteAllRecord();

    // other ---------------------------

    public Object getRoomsAndClientsByOwnershipId(Long id);

    public Object getRoomsAndClientsByOwnerId(Long id);

    public Object getRecordByApartmentAndFullName(String apartment, String fullName);
}
