package com.example.osbb.service.owner;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.owner.Owner;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService implements IOwnerService {
    private static final Logger log = Logger.getLogger(IOwnerService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;

    @Autowired
    private OwnerDAO ownerDAO;

    // one --------------------------------
    @Override
    @Transactional
    public Object createOwner(Owner owner) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Собственник с таким Ф.И.О. и датой рождения уже существует";
        log.info(messageEnter(methodName));
        try {
            if (!ownerDAO.existsByLastNameAndFirstNameAndSecondNameAndDateBirth(
                    owner.getLastName(),
                    owner.getFirstName(),
                    owner.getSecondName(),
                    owner.getDateBirth())) {
                owner.setActive(false);
                owner = ownerDAO.save(owner);
                messageResponse = "Создание собственника c ID : " + owner.getId() + " прошло успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(owner, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwner(Owner owner) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Собственник с ID : " + owner.getId() + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (ownerDAO.existsById(owner.getId())) {
                owner = ownerDAO.save(owner);
                messageResponse = "Обновление собственника с ID : " + owner.getId() + " прошло успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(owner, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getOwner(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Собственник с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            Owner owner = ownerDAO.findById(id).orElse(null);
            messageResponse = owner == null ? messageResponse : "Получение собственника с ID : " + id + " прошло успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(owner, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getOwnerByFullName(String fullName) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Собственник с ФИО : " + fullName + " не существует";
        log.info(messageEnter(methodName));
        try {
            String[] fios = fullName.split(" ");
            Owner owner = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);
            messageResponse = owner == null ? messageResponse : "Получение собственника с ФИО : " + fullName + " прошло успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(owner, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteOwner(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Собственник с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (ownerDAO.existsById(id)) {
                ownerDAO.deleteById(id);
                messageResponse = "Удаление собственника с ID : " + id + " прошло успешно";
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

    // all -----------------------
    @Override
    @Transactional
    public Object createAllOwner(List<Owner> owners) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Ни один из собственников не создан";
        log.info(messageEnter(methodName));
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner one : owners) {
                if (!ownerDAO.existsById(one.getId())) {
                    one.setActive(true);
                    one = ownerDAO.save(one);
                    log.info("Собственник с ID : " + one.getId() + " успешно создан");
                    result.add(one);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Создано " + result.size() + " собственников";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(sortedByLastName(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllOwner(List<Owner> owners) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Ни один из собственников не обновлён";
        log.info(messageEnter(methodName));
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner one : owners) {
                if (ownerDAO.existsById(one.getId())) {
                    one = ownerDAO.save(one);
                    log.info("Собственник с ID : " + one.getId() + " успешно обновлён");
                    result.add(one);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Обновлено " + result.size() + " собственников";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(sortedByLastName(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllOwner() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Owner> result = ownerDAO.findAll().stream().sorted(comparatorOwnerByLastName()).toList();
            String messageResponse = "Получено " + result.size() + " собственников";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result,List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwner() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Собственники удалены успешно";
        log.info(messageEnter(methodName));
        try {
            ownerDAO.deleteAll();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // count ------------------------------
    @Override
    public Object countOwners() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            long count = ownerDAO.count();
            String messageResponse = "Количество собственников составляет : " + count;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(count,List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // sorted -------------------------------------------------------------
    private List<Owner> sortedByLastName(List<Owner> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareTo(b.getLastName())).collect(Collectors.toList());
    }

    //.sorted(comparatorByBill())
    private Comparator<Owner> comparatorOwnerByLastName() {
        return (a, b) -> a.getLastName().compareTo(b.getLastName());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
