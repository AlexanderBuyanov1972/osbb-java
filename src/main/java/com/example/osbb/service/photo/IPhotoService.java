package com.example.osbb.service.photo;

import com.example.osbb.entity.owner.Photo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPhotoService {
    // ------------------- one -----------------------
    ResponseEntity<?> createPhoto(Photo photo);

    ResponseEntity<?> updatePhoto(Photo photo);

    ResponseEntity<?> getPhoto(Long id);

    ResponseEntity<?> deletePhoto(Long id);

    // ------------------ all ----------------

    ResponseEntity<?> createAllPhoto(List<Photo> photos);

    ResponseEntity<?> updateAllPhoto(List<Photo> photos);

    ResponseEntity<?> getAllPhoto();

    ResponseEntity<?> deleteAllPhoto();
}
