package com.example.osbb.service.role;

import com.example.osbb.dao.RoleDAO;
import com.example.osbb.dto.Auth;
import com.example.osbb.dto.ErrorResponseMessages;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ResponseMessages;
import com.example.osbb.entity.authorization.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleDAO roleDAO;

    // ---------------- one -----------------

    @Override
    @Transactional
    public Object createRole(Role role) {
        try {
            List<String> errors = new ArrayList<>();
            if (roleDAO.existsById(role.getId()))
                errors.add("Роль с таким ID уже существует.");
            if (roleDAO.existsByName(role.getName().name()))
                errors.add("Роль с таким именем уже существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(roleDAO.save(role))
                    .messages(List.of("Роль создана успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateRole(Role role) {
        try {
            List<String> errors = new ArrayList<>();
            if (!roleDAO.existsById(role.getId()))
                errors.add("Роль с таким ID не существует.");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(roleDAO.save(role))
                    .messages(List.of("Роль обновлена успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getRole(Long id) {
        try {
            if (roleDAO.existsById(id)) {
                return Response
                        .builder()
                        .data(roleDAO.findById(id).get())
                        .messages(List.of("Роль получена успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Роль с таким ID уже существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteRole(Long id) {
        try {
            if (roleDAO.existsById(id)) {
                roleDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Роль удалена успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Роль с таким ID уже существует."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // ------------------ all -----------------------

    @Override
    @Transactional
    public Object createAllRole(List<Role> roles) {
        try {
            List<Role> result = new ArrayList<>();
            for (Role role : roles) {
                if (!roleDAO.existsById(role.getId())) {
                    roleDAO.save(role);
                    result.add(role);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни одна из ролей создана не была. Роли с такими ID уже существуют."))
                    :  Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создано " + result.size() + " роли из " + roles.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllRole(List<Role> roles) {
        try {
            List<Role> result = new ArrayList<>();
            for (Role role : roles) {
                if (roleDAO.existsById(role.getId())) {
                    roleDAO.save(role);
                    result.add(role);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of("Ни одна из ролей обновлена не была. Ролей с такими ID не существует."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлено " + result.size() + " роли из " + roles.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllRole() {
        try {
            List<Role> result = roleDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных ролей не существует."))
                    : Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Все роли получены успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteAllRole() {
        try {
            roleDAO.deleteAll();
            return new ResponseMessages(List.of("Все роли успешно удалены."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Role> returnListSorted(List<Role> list){
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
