package com.example.osbb.service.owner;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Owner;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.entity.Ownership;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService implements IOwnerService {
    @Autowired
    private OwnerDAO ownerDAO;

    @Autowired
    private OwnershipDAO ownershipDAO;

    // ---------------- one -----------------
//    boolean existsByEmail(String email);
//    boolean existsByPhoneNumber(String phoneNumber);

    @Override
    @Transactional
    public Object createOwner(Owner owner) {
        try {
            List<String> errors = new ArrayList<>();
            if (ownerDAO.existsById(owner.getId())) {
                errors.add("Собственник с таким ID уже существует.");
            }
            if (ownerDAO.existsByEmail(owner.getEmail())) {
                errors.add("Собственник с таким E-mail уже существует.");
            }
            if (ownerDAO.existsByPhoneNumber(owner.getPhoneNumber())) {
                errors.add("Собственник с таким номером телефона уже существует.");
            }
            return !errors.isEmpty() ?
                    new ResponseMessages(errors)
                    : Response.builder()
                    .data(ownerDAO.save(owner))
                    .messages(List.of("Объект собственника создан успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwner(Owner owner) {
        try {
            List<String> errors = new ArrayList<>();
            if (!ownerDAO.existsById(owner.getId())) {
                errors.add("Собственник с таким ID не существует.");
            }
            if (!ownerDAO.existsByEmail(owner.getEmail())) {
                errors.add("Собственник с таким E-mail не существует.");
            }
            if (!ownerDAO.existsByPhoneNumber(owner.getPhoneNumber())) {
                errors.add("Собственник с таким номером телефона не существует.");
            }

            return !errors.isEmpty() ?
                    new ResponseMessages(errors)
                    : Response.builder()
                    .data(ownerDAO.save(owner))
                    .messages(List.of("Объект собственника обновлён успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getOwner(Long id) {
        try {
            return ownerDAO.existsById(id) ?
                    Response
                            .builder()
                            .data(ownerDAO.findById(id).get())
                            .messages(List.of("Собственник отправлен успешно.", "Удачного дня!"))
                            .build()
                    :
                    new ResponseMessages(List.of("Собственник с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteOwner(Long id) {
        try {
            if (ownerDAO.existsById(id)) {
                ownerDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Объект собственника удален успешно.", "Удачного дня!"))
                        .build();

            }
            return new ResponseMessages(List.of("Собственник с таким ID не существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // ------------------ all -----------------------

    @Override
    @Transactional
    public Object createAllOwner(List<Owner> owners) {
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner contact : owners) {
                if (!ownerDAO.existsById(contact.getId())) {
                    ownerDAO.save(contact);
                    result.add(contact);
                }
            }
            return result.isEmpty() ?
                    new ResponseMessages(List
                            .of("Ни один из собственников создан не был. Собственники с такими ID уже существуют.", "Удачного дня!"))
                    : Response
                    .builder()
                    .data(returnListSortedByLastName(result))
                    .messages(List.of("Успешно создано " + result.size() + " объектов собственника из " + owners.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllOwner(List<Owner> owners) {
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner contact : owners) {
                if (ownerDAO.existsById(contact.getId())) {
                    ownerDAO.save(contact);
                    result.add(contact);
                }
            }
            return result.isEmpty() ?
                    new ResponseMessages(List
                            .of("Ни один из собственников обновлён не был. Собственников с такими ID не существует."))
                    :
                    Response
                            .builder()
                            .data(returnListSortedByLastName(result))
                            .messages(List.of("Успешно обновлено " + result.size() + " объектов собственника из " + owners.size() + ".", "Удачного дня!"))
                            .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllOwner() {
        try {
            List<Owner> result = ownerDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных собственников не существует."))
                    : Response
                    .builder()
                    .data(returnListSortedByLastName(result))
                    .messages(List.of("Список собственников получен успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwner() {
        try {
            ownerDAO.deleteAll();
            return new ResponseMessages(List.of("Все собственники удалены успешно."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object countOwners() {
        long count = ownerDAO.count();
        try {
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество собственников составляет " + count + " человек.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    private List<Owner> returnListSortedByLastName(List<Owner> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareTo(b.getLastName())).collect(Collectors.toList());
    }

    // ---------------- addition functions ----------------

//    private List<Ownership> getListOwnershipByOwnerId(long id) {
//        List<Ownership> result = new ArrayList<>();
//        ownershipDAO.findAll().forEach(el -> {
//            el.getOwners().forEach(one -> {
//                if (one.getId() == id)
//                    result.add(el);
//            });
//        });
//        return result;
//    }


}
