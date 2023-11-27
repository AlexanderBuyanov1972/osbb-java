package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.entity.Record;
import com.example.osbb.service.record.IRecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.RECORD)
public class RecordController {
    private static final Logger log = Logger.getLogger(RecordController.class);

    @Autowired
    private IRecordService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody Record record) {
        log.info(messageEnter("createRecord"));
        return response.returnResponse(service.createRecord(record));
    }

    @PutMapping
    public ResponseEntity<?> updateRecord(@RequestBody Record record) {
        log.info(messageEnter("updateRecord"));
        return response.returnResponse(service.updateRecord(record));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getRecord(@PathVariable Long id) {
        log.info(messageEnter("getRecord"));
        return response.returnResponse(service.getRecord(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        log.info(messageEnter("deleteRecord"));
        return response.returnResponse(service.deleteRecord(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllRecord(@RequestBody List<Record> list) {
        log.info(messageEnter("createAllRecord"));
        return response.returnResponse(service.createAllRecord(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllRecord(@RequestBody List<Record> list) {
        log.info(messageEnter("updateAllRecord"));
        return response.returnResponse(service.updateAllRecord(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllRecord() {
        log.info(messageEnter("getAllRecord"));
        return response.returnResponse(service.getAllRecord());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllRecord() {
        log.info(messageEnter("deleteAllRecord"));
        return response.returnResponse(service.deleteAllRecord());
    }

    // other ---------------------------

    @GetMapping(ApiConstants.OWNERSHIP + ApiConstants.ID)
    public ResponseEntity<?> getRoomAndListClientByOwnershipId(@PathVariable Long id) {
        log.info(messageEnter("getRoomAndListClientByOwnershipId"));
        return response.returnResponse(service.getAllRecordByOwnershipId(id));
    }

    @GetMapping(ApiConstants.OWNER + ApiConstants.ID)
    public ResponseEntity<?> getClientAndListRoomByOwnerId(@PathVariable Long id) {
        log.info(messageEnter("getClientAndListRoomByOwnerId"));
        return response.returnResponse(service.getAllRecordByOwnerId(id));
    }

    @GetMapping(ApiConstants.PARAM_APARTMENT + ApiConstants.PARAM_FULL_NAME)
    public ResponseEntity<?> getRecordByApartmentAndFullName(
            @PathVariable String apartment,
            @PathVariable String fullName) {
        log.info(messageEnter("getRecordByApartmentAndFullName"));
        return response.returnResponse(service.getRecordByApartmentAndFullName(apartment, fullName));
    }

    @DeleteMapping(ApiConstants.PARAM_OWNER_ID + ApiConstants.PARAM_OWNERSHIP_ID)
    public ResponseEntity<?> deleteRecordByOwnerIdAndOwnershipId(
            @PathVariable Long ownerId,
            @PathVariable Long ownershipId) {
        log.info(messageEnter("deleteRecordByOwnerIdAndOwnershipId"));
        return response.returnResponse(service.deleteRecordByOwnerIdAndOwnershipId(ownerId, ownershipId));
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }


}
