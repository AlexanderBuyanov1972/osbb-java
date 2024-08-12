package com.example.osbb.service.photo;

import com.example.osbb.dao.owner.PhotoDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.Photo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhotoService implements IPhotoService {

    @Autowired
    private PhotoDAO photoDAO;

    @Override
    @Transactional
    public ResponseEntity<?> createPhoto(Photo photo) {
        String message = "Фото с ID : " + photo.getId() + " создано успешно";
        photo = photoDAO.save(photo);
        log.info(message);
        return ResponseEntity.ok(new Response(photo, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePhoto(Photo photo) {
        String message = "Фото с ID : " + photo.getId() + " не существует";
        if (photoDAO.existsById(photo.getId())) {
            photo = photoDAO.save(photo);
            message = "Фото с ID : " + photo.getId() + " обновлено успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(photo, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getPhoto(Long id) {
        String message = "Фото с ID : " + id + " не существует";
        Photo photo = photoDAO.findById(id).orElse(null);
        if (photo != null) {
            log.info("Фото с ID : {} получено успешно", id);
            log.info(message);
            return ResponseEntity.ok(new Response(photo, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deletePhoto(Long id) {
        String message = "Фото с ID : " + id + " не существует";
        if (photoDAO.existsById(id)) {
            photoDAO.deleteById(id);
            message = "Фото с ID : " + id + " удалено успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> createAllPhoto(List<Photo> photos) {
        String message = "Не создано ни одного фото";
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos) {
            if (!photoDAO.existsById(photo.getId())) {
                photo = photoDAO.save(photo);
                log.info("Фото с ID : {} создано успешно", photo.getId());
                result.add(photo);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " фото";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllPhoto(List<Photo> photos) {
        String message = "Не обновлено ни одного фото";
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos) {
            if (photoDAO.existsById(photo.getId())) {
                photo = photoDAO.save(photo);
                log.info("Фото с ID : {} обновлено успешно", photo.getId());
                result.add(photo);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " фото";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllPhoto() {
        List<Photo> result = photoDAO.findAll();
        String message = "Получено " + result.size() + " фото";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllPhoto() {
        photoDAO.deleteAll();
        String message = "Все фото удалены успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    // sorted ------------------------
    private List<Photo> listSorted(List<Photo> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
