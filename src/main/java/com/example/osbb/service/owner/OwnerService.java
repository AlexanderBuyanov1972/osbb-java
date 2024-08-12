package com.example.osbb.service.owner;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.Owner;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OwnerService implements IOwnerService {
    @Autowired
    private OwnerDAO ownerDAO;

    // one --------------------------------
    @Override
    @Transactional
    public ResponseEntity<?> createOwner(Owner owner) {
        String message = "Собственник с таким Ф.И.О. и датой рождения уже существует";
        if (!ownerDAO.existsByLastNameAndFirstNameAndSecondNameAndDateBirth(
                owner.getLastName(),
                owner.getFirstName(),
                owner.getSecondName(),
                owner.getDateBirth())) {
            owner.setActive(false);
            owner = ownerDAO.save(owner);
            message = "Создание собственника c ID : " + owner.getId() + " прошло успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(owner, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));

    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOwner(Owner owner) {
        String message = "Собственник с ID : " + owner.getId() + " не существует";
        if (ownerDAO.existsById(owner.getId())) {
            owner = ownerDAO.save(owner);
            message = "Обновление собственника с ID : " + owner.getId() + " прошло успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(owner, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getOwner(Long id) {
        String message = "Собственник с ID : " + id + " не существует";
        Owner owner = ownerDAO.findById(id).orElse(null);
        if (owner != null) {
            message = "Собственник " + mapOwnerToFullName(owner) + " получен успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(owner, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getOwnerByFullName(String fullName) {
        String message = "Собственник с ФИО : " + fullName + " не существует";
        String[] fios = fullName.split(" ");
        Owner owner = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);
        if (owner != null) {
            message = "Получение собственника с ФИО : " + fullName + " прошло успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(owner, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteOwner(Long id) {
        String message = "Собственник с ID : " + id + " не существует";
        if (ownerDAO.existsById(id)) {
            ownerDAO.deleteById(id);
            message = "Удаление собственника с ID : " + id + " прошло успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(id, List.of(message)));

    }

    // all -----------------------
    @Override
    @Transactional
    public ResponseEntity<?> createAllOwner(List<Owner> owners) {
        String message = "Ни один из собственников не создан";
        List<Owner> result = new ArrayList<>();
        for (Owner one : owners) {
            if (!ownerDAO.existsById(one.getId())) {
                one.setActive(true);
                one = ownerDAO.save(one);
                log.info("Собственник с ID : {} успешно создан", one.getId());
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Создано " + result.size() + " собственников";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedByLastName(result), List.of(message)));

    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllOwner(List<Owner> owners) {
        String message = "Ни один из собственников не обновлён";
        List<Owner> result = new ArrayList<>();
        for (Owner one : owners) {
            if (ownerDAO.existsById(one.getId())) {
                one = ownerDAO.save(one);
                log.info("Собственник с ID : " + one.getId() + " успешно обновлён");
                result.add(one);
            }
        }
        if (result.isEmpty()) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        message = "Обновлено " + result.size() + " собственников";
        log.info(message);
        return ResponseEntity.ok(new Response(sortedByLastName(result), List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllOwner() {
        List<Owner> result = ownerDAO.findAll().stream().sorted(comparatorByLastName()).toList();
        String message = "Получено " + result.size() + " собственников";
        log.info(message);
        return ResponseEntity.ok(new Response(result, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllOwner() {
        String message = "Собственники удалены успешно";
        ownerDAO.deleteAll();
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    // count ------------------------------
    @Override
    public ResponseEntity<?> countOwners() {
        long count = ownerDAO.count();
        String message = "Количество собственников составляет : " + count;
        log.info(message);
        return ResponseEntity.ok(new Response(count, List.of(message)));
    }

    public String mapOwnerToFullName(Owner o) {
        return o.getLastName() + " " + o.getFirstName() + " " + o.getSecondName();
    }

    public List<Owner> sortedByLastName(List<Owner> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareTo(b.getLastName())).collect(Collectors.toList());
    }

    public Comparator<Owner> comparatorByLastName() {
        return (a, b) -> a.getLastName().compareTo(b.getLastName());
    }

}
