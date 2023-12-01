package com.example.osbb.service.placework;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.PlaceWorkDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.owner.PlaceWork;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class PlaceWorkService implements IPlaceWorkService {
    private static final Logger log = Logger.getLogger(PlaceWorkService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    PlaceWorkDAO placeWorkDAO;

    @Override
    @Transactional
    public Object createPlaceWork(PlaceWork placeWork) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            placeWork = placeWorkDAO.save(placeWork);
            String messageResponse = "Место работы c ID : " + placeWork.getId() + " создано успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(placeWork,List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updatePlaceWork(PlaceWork placeWork) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Место работы c ID : " + placeWork.getId() + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (placeWorkDAO.existsById(placeWork.getId())) {
                placeWork = placeWorkDAO.save(placeWork);
                messageResponse = "Место работы c ID : " + placeWork.getId() + " обновлено успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(placeWork,List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getPlaceWork(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Место работы c ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            PlaceWork placeWork = placeWorkDAO.findById(id).orElse(null);
            if (placeWork != null)
                messageResponse = "Место работы с ID : " + id + " получен успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(placeWork, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deletePlaceWork(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Рабочее место с ID : " + id + " не найдено";
        log.info(messageEnter(methodName));
        try {
            if (placeWorkDAO.existsById(id)) {
                placeWorkDAO.deleteById(id);
                messageResponse = "Рабочее место с ID : " + id + " удалено успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(id,List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object createAllPlaceWork(List<PlaceWork> list) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не создано ни одного рабочего места";
        log.info(messageEnter(methodName));
        try {
            List<PlaceWork> result = new ArrayList<>();
            for (PlaceWork one : list) {
                if (!placeWorkDAO.existsById(one.getId())) {
                    one = placeWorkDAO.save(one);
                    log.info("Рабочее место с ID : " + one.getId() + " создано успешно");
                    result.add(one);
                }
            }
            if (!result.isEmpty())
                messageResponse = "Создано " + result.size() + " успешно";
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
    public Object updateAllPlaceWork(List<PlaceWork> list) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не обновлено ни одного рабочего места";
        log.info(messageEnter(methodName));
        try {
            List<PlaceWork> result = new ArrayList<>();
            for (PlaceWork one : list) {
                if (placeWorkDAO.existsById(one.getId())) {
                    one = placeWorkDAO.save(one);
                    log.info("Рабочее место с ID : " + one.getId() + " обновлено успешно");
                    result.add(one);
                }
            }
            if (!result.isEmpty())
                messageResponse = "Обновлено " + result.size() + " рабочих мест";
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
    public Object getAllPlaceWork() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<PlaceWork> result = placeWorkDAO.findAll().stream().sorted(comparatorById()).toList();
            String messageResponse = "Получено " + result.size() + " рабочих мест";
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
    public Object deleteAllPlaceWork() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Все рабочие места удалены успешно";
        log.info(messageEnter(methodName));
        try {
            placeWorkDAO.deleteAll();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // sorted --------------------
    private List<PlaceWork> listSorted(List<PlaceWork> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private Comparator<PlaceWork> comparatorById() {
        return (a, b) -> (int) (a.getId() - b.getId());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
