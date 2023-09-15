package com.example.osbb.controller;

import com.example.osbb.consts.ApiConstants;
import com.example.osbb.entity.Photo;
import com.example.osbb.service.photo.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.PHOTO)
public class PhotoController {

    @Autowired
    private IPhotoService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createPhoto(@RequestBody Photo photo) {
        return response.returnResponse(service.createPhoto(photo));
    }

    @PutMapping
    public ResponseEntity<?> updatePhoto(@RequestBody Photo photo) {
        return response.returnResponse(service.updatePhoto(photo));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getPhoto(@PathVariable Long id) {
        return response.returnResponse(service.getPhoto(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        return response.returnResponse(service.deletePhoto(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllPhoto(@RequestBody List<Photo> list) {
        return response.returnResponse(service.createAllPhoto(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllPhoto(@RequestBody List<Photo> list) {
        return response.returnResponse(service.updateAllPhoto(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllPhoto() {
        return response.returnResponse(service.getAllPhoto());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllPhoto() {
        return response.returnResponse(service.deleteAllPhoto());
    }


}
