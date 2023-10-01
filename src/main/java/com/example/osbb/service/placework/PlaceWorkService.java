package com.example.osbb.service.placework;

import com.example.osbb.dao.PlaceWorkDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.PlaceWork;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class PlaceWorkService implements IPlaceWorkService {

    @Autowired
    PlaceWorkDAO placeWorkDAO;

    @Override
    @Transactional
    public Object createPlaceWork(PlaceWork placeWork) {
        try {
            List<String> errors = new ArrayList<>();
            if (placeWorkDAO.existsById(placeWork.getId()))
                errors.add(ServiceMessages.ALREADY_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(placeWorkDAO.save(placeWork))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updatePlaceWork(PlaceWork placeWork) {
        try {
            List<String> errors = new ArrayList<>();
            if (!placeWorkDAO.existsById(placeWork.getId()))
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(placeWorkDAO.save(placeWork))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getPlaceWork(Long id) {
        try {
            if (placeWorkDAO.existsById(id)) {
                return Response
                        .builder()
                        .data(placeWorkDAO.findById(id).get())
                        .messages(List.of(ServiceMessages.OK))
                        .build();
            }
            return new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePlaceWork(Long id) {
        try {
            if (placeWorkDAO.existsById(id)) {
                placeWorkDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of(ServiceMessages.OK))
                        .build();
            }
            return new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllPlaceWork(List<PlaceWork> list) {
        try {
            List<PlaceWork> result = new ArrayList<>();
            for (PlaceWork one : list) {
                if (!placeWorkDAO.existsById(one.getId())) {
                    placeWorkDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllPlaceWork(List<PlaceWork> list) {
        try {
            List<PlaceWork> result = new ArrayList<>();
            for (PlaceWork one : list) {
                if (placeWorkDAO.existsById(one.getId())) {
                    placeWorkDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllPlaceWork() {
        try {
            List<PlaceWork> result = placeWorkDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object deleteAllPlaceWork() {
        try {
            placeWorkDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // sorted --------------------
    private List<PlaceWork> returnListSorted(List<PlaceWork> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
