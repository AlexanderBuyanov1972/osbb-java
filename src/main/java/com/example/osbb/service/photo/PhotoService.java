package com.example.osbb.service.photo;

import com.example.osbb.dao.PhotoDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Photo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService implements IPhotoService {
    @Autowired
    private PhotoDAO photoDAO;

    @Override
    @Transactional
    public Object createPhoto(Photo photo) {
        List<String> list = new ArrayList<>();
        try {
            if (photoDAO.existsById(photo.getId())) {
                list.add("Фото с таким ID уже существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(photoDAO.save(photo))
                    .messages(List.of("Создание фото прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updatePhoto(Photo photo) {
        List<String> list = new ArrayList<>();
        try {
            if (!photoDAO.existsById(photo.getId())) {
                list.add("Фото с таким ID не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(photoDAO.save(photo))
                    .messages(List.of("Обновление фото прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getPhoto(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (!photoDAO.existsById(id)) {
                list.add("Фото с таким ID не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(photoDAO.findById(id).get())
                    .messages(List.of("Получение фото прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePhoto(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (photoDAO.existsById(id)) {
                photoDAO.deleteById(id);
            } else {
                list.add("Фото с таким ID не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of("Удаление фото прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllPhoto(List<Photo> photos) {
        List<Photo> result = new ArrayList<>();
        try {
            for (Photo photo : photos) {
                if (!photoDAO.existsById(photo.getId())) {
                    result.add(photoDAO.save(photo));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни одна из фото создана не была. Фото с такими ID уже существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлено " + result.size() + " фото из " + photos.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllPhoto(List<Photo> photos) {
        List<Photo> result = new ArrayList<>();
        try {
            for (Photo photo : photos) {
                if (photoDAO.existsById(photo.getId())) {
                    result.add(photoDAO.save(photo));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни одна из фото обновлено не было. Фото с такими ID не существуют."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлено " + result.size() + " фото из " + photos.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getAllPhoto() {
        try {
            List<Photo> result = photoDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of("В базе данных нет ни одного фото по вашему запросу."))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of("Запрос выполнен успешно.", "Удачного дня!"))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllPhoto() {
        try {
            photoDAO.deleteAll();
            return new ResponseMessages(List.of("Все фото удалены успешно.", "Удачного дня!"));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    private List<Photo> returnListSorted(List<Photo> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
