package com.example.osbb.service.placework;

import com.example.osbb.dao.PlaceWorkDAO;
import com.example.osbb.dto.Auth;
import com.example.osbb.dto.ErrorResponseMessages;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ResponseMessages;
import com.example.osbb.entity.PlaceWork;
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
                errors.add("Рабочее место с таким id уже существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(placeWorkDAO.save(placeWork))
                    .messages(List.of("Рабочее место создано успешно.", "Удачного дня!"))
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
                errors.add("Рабочее место с таким id не существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(placeWorkDAO.save(placeWork))
                    .messages(List.of("Рабочее место обновлено успешно.", "Удачного дня!"))
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
                        .messages(List.of("Рабочее место получено успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Рабочее место с таким ID уже существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePlaceWork(Long id) {
        try {
            if(placeWorkDAO.existsById(id)){
                placeWorkDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Рабочее место удалено успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Рабочее место с таким ID уже существует."));
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
                    .of("Ни одно из рабочих мест создано не было. Рабочие места с такими ID уже существуют."))
                    :  Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создано " + result.size() + " рабочих мест из " + list.size() + ".", "Удачного дня!"))
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
                    .of("Ни одно из рабочих мест обновлено не было. Рабочие места с такими ID уже существуют."))
                    :  Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создано " + result.size() + " рабочих мест из " + list.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllPlaceWork() {
        try {
            List<PlaceWork> result = placeWorkDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных рабочих мест не существует."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Все рабочие места получены успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object deleteAllPlaceWork() {
        try {
            placeWorkDAO.deleteAll();
            return new ResponseMessages(List.of("Все рабочие места успешно удалены."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    private List<PlaceWork> returnListSorted(List<PlaceWork> list){
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
