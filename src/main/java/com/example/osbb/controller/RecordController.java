package com.example.osbb.controller;

import com.example.osbb.entity.Record;
import com.example.osbb.service.records.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.RECORD)
public class RecordController {

    @Autowired
    private IRecordService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody Record record) {
        return response.returnResponse(service.createRecord(record));
    }

    @PutMapping
    public ResponseEntity<?> updateRecord(@RequestBody Record record) {
        return response.returnResponse(service.updateRecord(record));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getRecord(@PathVariable Long id) {
        return response.returnResponse(service.getRecord(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        return response.returnResponse(service.deleteRecord(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllRecord(@RequestBody List<Record> list) {
        return response.returnResponse(service.createAllRecord(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllRecord(@RequestBody List<Record> list) {
        return response.returnResponse(service.updateAllRecord(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllRecord() {
        return response.returnResponse(service.getAllRecord());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllRecord() {
        return response.returnResponse(service.deleteAllRecord());
    }

    // other ---------------------------

    @GetMapping(ApiConstants.OWNERSHIP + ApiConstants.ID)
    public ResponseEntity<?> getRoomAndListClientByOwnershipId(@PathVariable Long id) {
        return response.returnResponse(service.getRoomsAndClientsByOwnershipId(id));
    }

    @GetMapping(ApiConstants.OWNER + ApiConstants.ID)
    public ResponseEntity<?> getClientAndListRoomByOwnerId(@PathVariable Long id) {
        return response.returnResponse(service.getRoomsAndClientsByOwnerId(id));
    }

    @GetMapping(ApiConstants.PARAM_4 + ApiConstants.PARAM_3)
    public ResponseEntity<?> getRecordByApartmentAndFullName(
            @PathVariable String apartment,
            @PathVariable String fullName) {
        return response.returnResponse(service.getRecordByApartmentAndFullName(apartment, fullName));
    }

}
