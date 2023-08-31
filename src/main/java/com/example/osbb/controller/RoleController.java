package com.example.osbb.controller;

import com.example.osbb.consts.ApiConstants;
import com.example.osbb.entity.authorization.Role;
import com.example.osbb.service.role.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.ROLE)
public class RoleController {

    @Autowired
    private IRoleService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        return response.returnResponse(service.createRole(role));
    }

    @PutMapping
    public ResponseEntity<?> updateRole(@RequestBody Role role) {
        return response.returnResponse(service.updateRole(role));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getRole(@PathVariable Long id) {
        return response.returnResponse(service.getRole(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        return response.returnResponse(service.deleteRole(id));
    }

    // ---------------------- all ----------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllRole(@RequestBody List<Role> list) {
        return response.returnResponse(service.createAllRole(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllRole(@RequestBody List<Role> list) {
        return response.returnResponse(service.updateAllRole(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllRole() {
        return response.returnResponse(service.getAllRole());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllRole() {
        return response.returnResponse(service.deleteAllRole());
    }

}
