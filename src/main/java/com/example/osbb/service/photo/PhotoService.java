package com.example.osbb.service.photo;

import com.example.osbb.dao.owner.PhotoDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Photo;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService implements IPhotoService {
    private static final Logger log = Logger.getLogger(PhotoService.class);
    @Autowired
    private PhotoDAO photoDAO;

    @Override
    @Transactional
    public Object createPhoto(Photo photo) {
        log.info("Method createPhoto : enter");
        try {
            photo = photoDAO.save(photo);
            log.info("Фото с ID : " + photo.getId() + " создано успешно");
            log.info("Method createPhoto : exit");
            return Response
                    .builder()
                    .data(photo)
                    .messages(List.of("Фото с ID : " + photo.getId() + " создано успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updatePhoto(Photo photo) {
        log.info("Method updatePhoto : enter");
        try {
            if (!photoDAO.existsById(photo.getId())) {
                log.info("Фото с ID : " + photo.getId() + " не существует");
                log.info("Method updatePhoto : exit");
                return new ResponseMessages(List.of("Фото с ID : " + photo.getId() + " не существует"));
            }
            photo = photoDAO.save(photo);
            log.info("Фото с ID : " + photo.getId() + " создано успешно");
            log.info("Method updatePhoto : exit");
            return Response
                    .builder()
                    .data(photo)
                    .messages(List.of("Фото с ID : " + photo.getId() + " создано успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getPhoto(Long id) {
        log.info("Method getPhoto : enter");
        try {
            if (!photoDAO.existsById(id)) {
                log.info("Фото с ID : " + id + " не существует");
                log.info("Method getPhoto : exit");
                return new ResponseMessages(List.of("Фото с ID : " + id + " не существует"));
            }
            Photo photo = photoDAO.findById(id).get();
            log.info("Фото с ID : " + id + " получено успешно");
            log.info("Method getPhoto : exit");
            return Response
                    .builder()
                    .data(photo)
                    .messages(List.of("Фото с ID : " + id + " получено успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePhoto(Long id) {
        log.info("Method deletePhoto : enter");
        try {
            if (photoDAO.existsById(id)) {
                photoDAO.deleteById(id);
                log.info("Фото с ID : " + id + " удалено успешно");
                log.info("Method deletePhoto : exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Фото с ID : " + id + " удалено успешно"))
                        .build();
            } else {
                log.info("Фото с ID : " + id + " не существует");
                log.info("Method deletePhoto : exit");
                return new ResponseMessages(List.of("Фото с ID : " + id + " не существует"));
            }

        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllPhoto(List<Photo> photos) {
        log.info("Method createAllPhoto : enter");
        List<Photo> result = new ArrayList<>();
        try {
            for (Photo photo : photos) {
                if (!photoDAO.existsById(photo.getId())) {
                    photo = photoDAO.save(photo);
                    log.info("Фото с ID : " + photo.getId() + " создано успешно");
                    result.add(photo);
                }
            }
            if (result.isEmpty()) {
                log.info("Не создано ни одного фото");
                log.info("Method createAllPhoto : exit");
                return new ResponseMessages(List.of("Не создано ни одного фото"));
            }
            log.info("Создано " + result.size() + " фото");
            log.info("Method createAllPhoto : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Создано " + result.size() + " фото"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllPhoto(List<Photo> photos) {
        log.info("Method updateAllPhoto : enter");
        List<Photo> result = new ArrayList<>();
        try {
            for (Photo photo : photos) {
                if (photoDAO.existsById(photo.getId())) {
                    photo = photoDAO.save(photo);
                    log.info("Фото с ID : " + photo.getId() + " обновлено успешно");
                    result.add(photo);
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлено ни одного фото");
                log.info("Method updateAllPhoto : exit");
                return new ResponseMessages(List.of("Не обновлено ни одного фото"));
            }
            log.info("Обновлено " + result.size() + " фото");
            log.info("Method updateAllPhoto : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Обновлено " + result.size() + " фото"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAllPhoto() {
        log.info("Method getAllPhoto : enter");
        try {
            List<Photo> result = photoDAO.findAll();
            log.info("Получено " + result.size() + " фото");
            log.info("Method getAllPhoto : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Получено " + result.size() + " фото"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllPhoto() {
        log.info("Method deleteAllPhoto : enter");
        try {
            photoDAO.deleteAll();
            log.info("Все фото удалены успешно");
            log.info("Method deleteAllPhoto : exit");
            return new ResponseMessages(List.of("Все фото удалены успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // sorted ------------------------
    private List<Photo> listSorted(List<Photo> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
