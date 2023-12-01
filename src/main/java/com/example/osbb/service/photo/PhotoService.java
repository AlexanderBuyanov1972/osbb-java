package com.example.osbb.service.photo;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.PhotoDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
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
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private PhotoDAO photoDAO;

    @Override
    @Transactional
    public Object createPhoto(Photo photo) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Фото с ID : " + photo.getId() + " создано успешно";
        log.info(messageEnter(methodName));
        try {
            photo = photoDAO.save(photo);
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(photo, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updatePhoto(Photo photo) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Фото с ID : " + photo.getId() + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (photoDAO.existsById(photo.getId())) {
                photo = photoDAO.save(photo);
                messageResponse = "Фото с ID : " + photo.getId() + " обновлено успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(photo, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getPhoto(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Фото с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            Photo photo = photoDAO.findById(id).orElse(null);
            if (photo != null)
                log.info("Фото с ID : " + id + " получено успешно");
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(photo, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePhoto(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Фото с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (photoDAO.existsById(id)) {
                photoDAO.deleteById(id);
                messageResponse = "Фото с ID : " + id + " удалено успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(id, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllPhoto(List<Photo> photos) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Не создано ни одного фото";
        List<Photo> result = new ArrayList<>();
        try {
            for (Photo photo : photos) {
                if (!photoDAO.existsById(photo.getId())) {
                    photo = photoDAO.save(photo);
                    log.info("Фото с ID : " + photo.getId() + " создано успешно");
                    result.add(photo);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Создано " + result.size() + " фото";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(listSorted(result),List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllPhoto(List<Photo> photos) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не обновлено ни одного фото";
        log.info(messageEnter(methodName));
        List<Photo> result = new ArrayList<>();
        try {
            for (Photo photo : photos) {
                if (photoDAO.existsById(photo.getId())) {
                    photo = photoDAO.save(photo);
                    log.info("Фото с ID : " + photo.getId() + " обновлено успешно");
                    result.add(photo);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Обновлено " + result.size() + " фото";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(listSorted(result),List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllPhoto() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Photo> result = photoDAO.findAll();
            String messageResponse = "Получено " + result.size() + " фото";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(listSorted(result),List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllPhoto() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            photoDAO.deleteAll();
            String messageResponse = "Все фото удалены успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // sorted ------------------------
    private List<Photo> listSorted(List<Photo> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
