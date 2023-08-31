package com.example.osbb.service.role;

import com.example.osbb.dao.RoleDAO;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.authorization.Role;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.consts.ObjectMessages;
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
    public Object createRole(Role role) {
        try {
            List<String> list = new ArrayList<>();
            if (roleDAO.existsById(role.getId()))
                list.add(ObjectMessages.withSuchIdAlreadyExists("Role"));
            if (roleDAO.existsByName(role.getName().name()))
                list.add(ObjectMessages.roleWithSuchNameAlreadyExists());
            return list.isEmpty() ? List.of(roleDAO.save(role)) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateRole(Role role) {
        try {
            List<String> list = new ArrayList<>();
            if (!roleDAO.existsById(role.getId()))
                list.add(ObjectMessages.withSuchIdNotExists("Role"));
            return list.isEmpty() ? List.of(roleDAO.save(role)) : new ErrorResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getRole(Long id) {
        try {
            if (roleDAO.existsById(id)) {
                return List.of(roleDAO.findById(id).get());
            }
            return new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Role")));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object deleteRole(Long id) {
        try {
            if (roleDAO.existsById(id)) {
                roleDAO.deleteById(id);
                return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
            }
            return new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Role")));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // ------------------ all -----------------------

    @Override
    public Object createAllRole(List<Role> list) {
        try {
            List<Role> result = new ArrayList<>();
            for (Role role : list) {
                if (!roleDAO.existsById(role.getId())) {
                    roleDAO.save(role);
                    result.add(role);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List.of(ObjectMessages.noObjectCreated("Role")))
                    :  returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateAllRole(List<Role> list) {
        try {
            List<Role> result = new ArrayList<>();
            for (Role role : list) {
                if (roleDAO.existsById(role.getId())) {
                    roleDAO.save(role);
                    result.add(role);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List.of(ObjectMessages.noObjectUpdated("Role")))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllRole() {
        try {
            List<Role> result = roleDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of(ObjectMessages.listEmpty()))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object deleteAllRole() {
        try {
            roleDAO.deleteAll();
            return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<Role> returnListSorted(List<Role> list){
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
