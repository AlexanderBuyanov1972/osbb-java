package com.example.osbb.service.placework;

import com.example.osbb.dao.owner.PlaceWorkDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.PlaceWork;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class PlaceWorkService implements IPlaceWorkService {
    private static final Logger log = LogManager.getLogger("OwnerService");

    @Autowired
    PlaceWorkDAO placeWorkDAO;

    @Override
    @Transactional
    public Object createPlaceWork(PlaceWork placeWork) {
        log.info("Method createPlaceWork : enter");
        try {
            placeWork = placeWorkDAO.save(placeWork);
            log.info("Место работы создано успешно");
            log.info("Method createPlaceWork : exit");
            return Response
                    .builder()
                    .data(placeWork)
                    .messages(List.of("Место работы создано успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updatePlaceWork(PlaceWork placeWork) {
        log.info("Method createPlaceWork : enter");
        try {
            if (!placeWorkDAO.existsById(placeWork.getId())) {
                log.info("Место работы c ID : " + placeWork.getId() + " не существует");
                log.info("Method updatePlaceWork : exit");
                return new ResponseMessages(List.of("Место работы c ID : " + placeWork.getId() + " не существует"));
            }
            placeWork = placeWorkDAO.save(placeWork);
            log.info("Рабочее место обновлено успешно");
            log.info("Method updatePlaceWork : exit");
            return Response
                    .builder()
                    .data(placeWork)
                    .messages(List.of("Рабочее место обновлено успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getPlaceWork(Long id) {
        log.info("Method getPlaceWork : enter");
        try {
            if (placeWorkDAO.existsById(id)) {
                PlaceWork placeWork = placeWorkDAO.findById(id).get();
                log.info("Место работы с ID : " + id + " получен успешно");
                log.info("Method getPlaceWork : exit");
                return Response
                        .builder()
                        .data(placeWork)
                        .messages(List.of("Место работы с ID : " + id + " получен успешно"))
                        .build();
            }
            log.info("Место работы c ID : " + id + " не существует");
            log.info("Method getPlaceWork : exit");
            return new ResponseMessages(List.of("Место работы c ID : " + id + " не существует"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePlaceWork(Long id) {
        log.info("Method deletePlaceWork : enter");
        try {
            if (placeWorkDAO.existsById(id)) {
                placeWorkDAO.deleteById(id);
                log.info("Рабочее место с ID : " + id + " удалено успешно");
                log.info("Method deletePlaceWork : exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Рабочее место с ID : " + id + " удалено успешно"))
                        .build();
            }
            log.info("Рабочее место с ID : " + id + " не существует");
            log.info("Method deletePlaceWork : exit");
            return new ResponseMessages(List.of("Рабочее место с ID : " + id + " не существует"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllPlaceWork(List<PlaceWork> list) {
        log.info("Method createAllPlaceWork : enter");
        try {
            List<PlaceWork> result = new ArrayList<>();
            for (PlaceWork one : list) {
                if (!placeWorkDAO.existsById(one.getId())) {
                    one = placeWorkDAO.save(one);
                    log.info("Рабочее место с ID : " + one.getId() + " создано успешно");
                    result.add(one);
                }
            }
            if (result.isEmpty()) {
                log.info("Не создано ни одного рабочего места");
                log.info("Method createAllPlaceWork : exit");
                return new ResponseMessages(List.of("Не создано ни одного рабочего места"));
            }
            log.info("Создано " + result.size() + " успешно");
            log.info("Method createAllPlaceWork : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Создано " + result.size() + " успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllPlaceWork(List<PlaceWork> list) {
        log.info("Method updateAllPlaceWork : enter");
        try {
            List<PlaceWork> result = new ArrayList<>();
            for (PlaceWork one : list) {
                if (placeWorkDAO.existsById(one.getId())) {
                    one = placeWorkDAO.save(one);
                    log.info("Рабочее место с ID : " + one.getId() + " обновлено успешно");
                    result.add(one);
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлено ни одного рабочего места");
                log.info("Method updateAllPlaceWork : exit");
                return new ResponseMessages(List.of("Не обновлено ни одного рабочего места"));
            }
            log.info("Обновлено " + result.size() + " рабочих мест");
            log.info("Method updateAllPlaceWork : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Обновлено " + result.size() + " рабочих мест"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAllPlaceWork() {
        log.info("Method getAllPlaceWork : enter");
        try {
            List<PlaceWork> result = placeWorkDAO.findAll();
            log.info("Получено " + result.size() + " рабочих мест");
            log.info("Method getAllPlaceWork : exit");
            return Response
                    .builder()
                    .data(listSorted(result))
                    .messages(List.of("Получено " + result.size() + " рабочих мест"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object deleteAllPlaceWork() {
        log.info("Method deleteAllPlaceWork : enter");
        try {
            placeWorkDAO.deleteAll();
            log.info("Все рабочие места удалены успешно");
            log.info("Method deleteAllPlaceWork : exit");
            return new ResponseMessages(List.of("Все рабочие места удалены успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // sorted --------------------
    private List<PlaceWork> listSorted(List<PlaceWork> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
