package com.example.osbb.controller;

import com.example.osbb.entity.Share;
import com.example.osbb.service.share.IShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.SHARE)
public class ShareController {

    @Autowired
    private IShareService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createShare(@RequestBody Share share) {
        return response.returnResponse(service.createShare(share));
    }

    @PutMapping
    public ResponseEntity<?> updateShare(@RequestBody Share share) {
        return response.returnResponse(service.updateShare(share));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getShare(@PathVariable Long id) {
        return response.returnResponse(service.getShare(id));
    }

    @GetMapping(ApiConstants.PARAM_APARTMENT + ApiConstants.PARAM_FULL_NAME)
    public ResponseEntity<?> getShareByApartmentAndFullName(
            @PathVariable String apartment,
            @PathVariable String fullName) {
        return response.returnResponse(service.getShareByApartmentAndFullName(apartment, fullName));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteShare(@PathVariable Long id) {
        return response.returnResponse(service.deleteShare(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllShare(@RequestBody List<Share> list) {
        return response.returnResponse(service.createAllShare(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllShare(@RequestBody List<Share> list) {
        return response.returnResponse(service.updateAllShare(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllShare() {
        return response.returnResponse(service.getAllShare());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllShare() {
        return response.returnResponse(service.deleteAllShare());
    }

    @DeleteMapping(ApiConstants.PARAM_OWNER_ID + ApiConstants.PARAM_OWNERSHIP_ID)
    public ResponseEntity<?> deleteShareByOwnerIdAndOwnershipId(
            @PathVariable Long ownerId,
            @PathVariable Long ownershipId
    ) {
        return response.returnResponse(service.deleteShareByOwnerIdAndOwnershipId(ownerId, ownershipId));
    }


}
