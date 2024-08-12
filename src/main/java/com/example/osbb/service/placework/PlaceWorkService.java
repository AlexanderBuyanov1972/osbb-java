package com.example.osbb.service.placework;

import com.example.osbb.dao.owner.PlaceWorkDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.PlaceWork;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
@Slf4j
public class PlaceWorkService implements IPlaceWorkService {

    @Autowired
    PlaceWorkDAO placeWorkDAO;

    @Override
    @Transactional
    public ResponseEntity<?> createPlaceWork(PlaceWork placeWork) {
        placeWork = placeWorkDAO.save(placeWork);
        String message = "Место работы c ID : " + placeWork.getId() + " создано успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(placeWork, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePlaceWork(PlaceWork placeWork) {
        String message = "Место работы c ID : " + placeWork.getId() + " не существует";
        if (placeWorkDAO.existsById(placeWork.getId())) {
            placeWork = placeWorkDAO.save(placeWork);
            message = "Место работы c ID : " + placeWork.getId() + " обновлено успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(placeWork, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getPlaceWork(Long id) {
        String message = "Место работы c ID : " + id + " не существует";
        PlaceWork placeWork = placeWorkDAO.findById(id).orElse(null);
        if (placeWork != null) {
            message = "Место работы с ID : " + id + " получен успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(placeWork, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deletePlaceWork(Long id) {
        String message = "Рабочее место с ID : " + id + " не найдено";
        if (placeWorkDAO.existsById(id)) {
            placeWorkDAO.deleteById(id);
            message = "Рабочее место с ID : " + id + " удалено успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> createAllPlaceWork(List<PlaceWork> list) {
        String message = "Не создано ни одного рабочего места";
        List<PlaceWork> result = new ArrayList<>();
        for (PlaceWork one : list) {
            if (!placeWorkDAO.existsById(one.getId())) {
                one = placeWorkDAO.save(one);
                log.info("Рабочее место с ID : " + one.getId() + " создано успешно");
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllPlaceWork(List<PlaceWork> list) {
        String message = "Не обновлено ни одного рабочего места";
        List<PlaceWork> result = new ArrayList<>();
        for (PlaceWork one : list) {
            if (placeWorkDAO.existsById(one.getId())) {
                one = placeWorkDAO.save(one);
                log.info("Рабочее место с ID : {} обновлено успешно", one.getId());
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " рабочих мест";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllPlaceWork() {
        List<PlaceWork> result = placeWorkDAO.findAll().stream().sorted(comparatorById()).toList();
        String message = "Получено " + result.size() + " рабочих мест";
        log.info(message);
        return ResponseEntity.ok(new Response(listSorted(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> deleteAllPlaceWork() {
        String message = "Все рабочие места удалены успешно";
        placeWorkDAO.deleteAll();
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    // sorted --------------------
    private List<PlaceWork> listSorted(List<PlaceWork> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private Comparator<PlaceWork> comparatorById() {
        return (a, b) -> (int) (a.getId() - b.getId());
    }

}
