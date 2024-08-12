package com.example.osbb.service.record;

import com.example.osbb.entity.Record;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRecordService {
    //  one -----------------------------
    ResponseEntity<?> createRecord(Record record);

    ResponseEntity<?> updateRecord(Record record);

    ResponseEntity<?> getRecord(Long id);

    ResponseEntity<?> deleteRecord(Long id);

    // all ----------------------------

    ResponseEntity<?> createAllRecord(List<Record> records);

    ResponseEntity<?> updateAllRecord(List<Record> records);

    ResponseEntity<?> getAllRecord();

    ResponseEntity<?> deleteAllRecord();

    // other ---------------------------

    ResponseEntity<?> getAllRecordByOwnershipId(Long id);

    ResponseEntity<?> getAllRecordByOwnerId(Long id);

    ResponseEntity<?> getRecordByApartmentAndFullName(String apartment, String fullName);

    ResponseEntity<?> deleteRecordByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId);
}
