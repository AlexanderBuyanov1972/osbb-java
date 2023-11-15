package com.example.osbb.service.owner;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.dto.pojo.Client;
import com.example.osbb.entity.owner.Owner;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService implements IOwnerService {
    private static final Logger log = Logger.getLogger(IOwnerService.class);

    @Autowired
    private OwnerDAO ownerDAO;

    // one --------------------------------
    @Override
    @Transactional
    public Object createOwner(Owner owner) {
        log.info("Method createOwner : enter");
        try {
            List<String> errors = new ArrayList<>();
            if (ownerDAO.existsByLastNameAndFirstNameAndSecondNameAndDateBirth(
                    owner.getLastName(),
                    owner.getFirstName(),
                    owner.getSecondName(),
                    owner.getDateBirth())) {
                log.info("Собственник с таким Ф.И.О. и датой рождения уже существует");
                errors.add("Собственник с таким Ф.И.О. и датой рождения уже существует");
            }

            if (ownerDAO.existsById(owner.getId())) {
                log.info("Собственник с таким ID : " + owner.getId() + " уже существует");
                errors.add("Собственник с таким ID : " + owner.getId() + " уже существует");
            }
            owner.setActive(false);
            log.info("Method createOwner : exit");
            if (!errors.isEmpty()) {
                log.info("Method createOwner : exit");
                return new ResponseMessages(errors);
            }
            owner = ownerDAO.save(owner);
            log.info("Method createOwner : exit");
            return Response.builder()
                    .data(owner)
                    .messages(List.of("Создание собственника прошло успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwner(Owner owner) {
        log.info("Method updateOwner : enter");
        try {
            if (!ownerDAO.existsById(owner.getId())) {
                log.info("Собственник с ID : " + owner.getId() + " не существует");
                log.info("Method updateOwner : exit");
                return new ResponseMessages(List.of("Собственник с ID : " + owner.getId() + " не существует"));
            }
            owner = ownerDAO.save(owner);
            log.info("Обновление собственника прошло успешно");
            log.info("Method updateOwner : enter");
            return Response.builder()
                    .data(owner)
                    .messages(List.of("Обновление собственника прошло успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getOwner(Long id) {
        log.info("Method getOwner : enter");
        try {
            if (!ownerDAO.existsById(id)) {
                log.info("Собственник с ID : " + id + " не существует");
                log.info("Method getOwner : exit");
                return new ResponseMessages(List.of("Собственник с ID : " + id + " не существует"));
            }
            Owner owner = ownerDAO.findById(id).get();
            log.info("Получение собственника с ID : " + id + " прошло успешно");
            log.info("Method getOwner : exit");
            return Response.builder()
                    .data(owner)
                    .messages(List.of("Получение собственника с ID : " + id + " прошло успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getOwnerByFullName(String fullName) {
        log.info("Method getOwnerByFullName : enter");
        try {
            String[] fios = fullName.split(" ");
            Owner owner = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);
            log.info("Получение собственника с ФИО : " + fullName + " прошло успешно");
            log.info("Method getOwnerByFullName : exit");
            return Response
                    .builder()
                    .data(owner)
                    .messages(List.of("Получение собственника с ФИО : " + fullName + " прошло успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteOwner(Long id) {
        log.info("Method deleteOwner : enter");
        try {
            if (ownerDAO.existsById(id)) {
                ownerDAO.deleteById(id);
                log.info("Удаление собственника с ID : " + id + " прошло успешно");
                log.info("Method deleteOwner : exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Удаление собственника с ID : " + id + " прошло успешно"))
                        .build();
            }
            log.info("Собственник с ID : " + id + " не существует");
            log.info("Method deleteOwner : exit");
            return new ResponseMessages(List.of("Собственник с ID : " + id + " не существует"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // all -----------------------
    @Override
    @Transactional
    public Object createAllOwner(List<Owner> owners) {
        log.info("Method createAllOwner : enter");
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner one : owners) {
                if (!ownerDAO.existsById(one.getId())) {
                    one.setActive(true);
                    result.add(ownerDAO.save(one));
                }
            }
            if (result.isEmpty()) {
                log.info("Ни один из собственников не создан");
                log.info("Method createAllOwner : exit");
                return new ResponseMessages(List.of("Ни один из собственников не создан"));

            }
            log.info("Создано " + result.size() + " собственников");
            log.info("Method createAllOwner : exit");
            return Response
                    .builder()
                    .data(sortedByLastName(result))
                    .messages(List.of("Создано " + result.size() + " собственников"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllOwner(List<Owner> owners) {
        log.info("Method updateAllOwner : enter");
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner contact : owners) {
                if (ownerDAO.existsById(contact.getId())) {
                    ownerDAO.save(contact);
                    result.add(contact);
                }
            }
            if (result.isEmpty()) {
                log.info("Ни один из собственников не обновлён");
                log.info("Method updateAllOwner : exit");
                return new ResponseMessages(List.of("Ни один из собственников не обновлён"));
            }
            log.info("Обновлено " + result.size() + " собственников");
            log.info("Method updateAllOwner : exit");
            return Response
                    .builder()
                    .data(sortedByLastName(result))
                    .messages(List.of("Обновлено " + result.size() + " собственников"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAllOwner() {
        log.info("Method getAllOwner : enter");
        try {
            List<Client> result = ownerDAO.findAll()
                    .stream()
                    .map(s -> new Client(s, Double.parseDouble("0")))
                    .sorted((a, b) -> a.getLastName().compareTo(b.getLastName()))
                    .toList();
            log.info("Получено " + result.size() + " собственников");
            log.info("Method getAllOwner : exit");
            return Response
                    .builder()
                    .data(result)
                    .messages(List.of("Получено " + result.size() + " собственников"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwner() {
        log.info("Method deleteAllOwner : enter");
        try {
            ownerDAO.deleteAll();
            log.info("Собственники удалены успешно");
            log.info("Method deleteAllOwner : exit");
            return new ResponseMessages(List.of("Собственники удалены успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // count ------------------------------
    @Override
    public Object countOwners() {
        log.info("Method countOwners : enter");
        try {
            long count = ownerDAO.count();
            log.info("Количество собственников составляет : " + count);
            log.info("Method countOwners : exit");
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Количество собственников составляет : " + count))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // sorted -------------------------------------------------------------
    private List<Owner> sortedByLastName(List<Owner> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareTo(b.getLastName())).collect(Collectors.toList());
    }

}
