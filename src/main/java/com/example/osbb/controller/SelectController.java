package com.example.osbb.controller;

import com.example.osbb.consts.ApiConstants;
import com.example.osbb.entity.Address;
import com.example.osbb.entity.Select;
import com.example.osbb.service.select.ISelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.SELECT)
public class SelectController {
    @Autowired
    private ISelectService service;
    @Autowired
    private HelpMethodsForController response;

    @PostMapping
    public ResponseEntity<?> createSelect(@RequestBody Select select) {
        return response.returnResponse(service.createSelect(select));
    }

    @PutMapping
    public ResponseEntity<?> updateSelect(@RequestBody Select select) {
        return response.returnResponse(service.updateSelect(select));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getSelect(@PathVariable Long id) {
        return response.returnResponse(service.getSelect(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteSelect(@PathVariable Long id) {
        return response.returnResponse(service.deleteSelect(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllSelect(@RequestBody List<Select> list) {
        return response.returnResponse(service.createAllSelect(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateAllSelect(@RequestBody List<Select> list) {
        return response.returnResponse(service.updateAllSelect(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllSelect() {
        return response.returnResponse(service.getAllSelect());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllSelect() {
        return response.returnResponse(service.deleteAllSelect());
    }

}
