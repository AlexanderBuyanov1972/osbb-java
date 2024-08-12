package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.Record;
import com.example.osbb.service.record.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.RECORD)
public class RecordController {

    @Autowired
    private IRecordService service;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody Record record) {
        return service.createRecord(record);
    }

    @PutMapping
    public ResponseEntity<?> updateRecord(@RequestBody Record record) {
        return service.updateRecord(record);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getRecord(@PathVariable Long id) {
        return service.getRecord(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        return service.deleteRecord(id);
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllRecord(@RequestBody List<Record> list) {
        return service.createAllRecord(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllRecord(@RequestBody List<Record> list) {
        return service.updateAllRecord(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllRecord() {
        return service.getAllRecord();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllRecord() {
        return service.deleteAllRecord();
    }

    // other ---------------------------

    @GetMapping(ApiPaths.OWNERSHIP + ApiPaths.PARAM_ID)
    public ResponseEntity<?> getRoomAndListClientByOwnershipId(@PathVariable Long id) {
        return service.getAllRecordByOwnershipId(id);
    }

    @GetMapping(ApiPaths.OWNER + ApiPaths.PARAM_ID)
    public ResponseEntity<?> getClientAndListRoomByOwnerId(@PathVariable Long id) {
        return service.getAllRecordByOwnerId(id);
    }

    @GetMapping(ApiPaths.PARAM_APARTMENT + ApiPaths.PARAM_FULL_NAME)
    public ResponseEntity<?> getRecordByApartmentAndFullName(
            @PathVariable String apartment,
            @PathVariable String fullName) {
        return service.getRecordByApartmentAndFullName(apartment, fullName);
    }

    @DeleteMapping(ApiPaths.PARAM_OWNER_ID + ApiPaths.PARAM_OWNERSHIP_ID)
    public ResponseEntity<?> deleteRecordByOwnerIdAndOwnershipId(
            @PathVariable Long ownerId,
            @PathVariable Long ownershipId) {
        return service.deleteRecordByOwnerIdAndOwnershipId(ownerId, ownershipId);
    }
}
