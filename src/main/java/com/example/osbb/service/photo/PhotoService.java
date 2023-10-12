package com.example.osbb.service.photo;

import com.example.osbb.dao.owner.PhotoDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Photo;
import com.example.osbb.service.ServiceMessages;
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
        try {
            return Response
                    .builder()
                    .data(photoDAO.save(photo))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
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
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(photoDAO.save(photo))
                    .messages(List.of(ServiceMessages.OK))
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
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(photoDAO.findById(id).orElse(new Photo()))
                    .messages(List.of(ServiceMessages.OK))
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
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
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
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of(ServiceMessages.OK))
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
                    .of(ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of(ServiceMessages.OK))
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
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(listSorted(result))
                            .messages(List.of(ServiceMessages.OK))
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
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // sorted ------------------------
    private List<Photo> listSorted(List<Photo> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
