package com.example.osbb.controller.owner;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.owner.Photo;
import com.example.osbb.service.photo.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.PHOTO)
public class PhotoController {

    @Autowired
    private IPhotoService service;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createPhoto(@RequestBody Photo photo) {
        return service.createPhoto(photo);
    }

    @PutMapping
    public ResponseEntity<?> updatePhoto(@RequestBody Photo photo) {
        return service.updatePhoto(photo);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getPhoto(@PathVariable Long id) {
        return service.getPhoto(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        return service.deletePhoto(id);
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllPhoto(@RequestBody List<Photo> list) {
        return service.createAllPhoto(list);
    }

    @PutMapping(ApiPaths.ALL)
    public ResponseEntity<?> updateAllPhoto(@RequestBody List<Photo> list) {
        return service.updateAllPhoto(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllPhoto() {
        return service.getAllPhoto();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllPhoto() {
        return service.deleteAllPhoto();
    }


}
