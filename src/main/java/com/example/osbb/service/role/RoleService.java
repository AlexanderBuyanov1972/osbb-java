package com.example.osbb.service.role;

import com.example.osbb.dao.RoleDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.authorization.Role;
import com.example.osbb.service.ServiceMessages;
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
                errors.add(ServiceMessages.ALREADY_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(roleDAO.save(role))
                    .messages(List.of(ServiceMessages.OK))
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
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(roleDAO.save(role))
                    .messages(List.of(ServiceMessages.OK))
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
                        .messages(List.of(ServiceMessages.OK))
                        .build();
            }
            return new ResponseMessages(List.of(ServiceMessages.ALREADY_EXISTS));
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
                        .messages(List.of(ServiceMessages.OK))
                        .build();
            }
            return new ResponseMessages(List.of(ServiceMessages.ALREADY_EXISTS));
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
    public Object getAllRole() {
        try {
            List<Role> result = roleDAO.findAll();
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
    @Transactional
    public Object deleteAllRole() {
        try {
            roleDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // sorted ----------------------
    private List<Role> returnListSorted(List<Role> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
