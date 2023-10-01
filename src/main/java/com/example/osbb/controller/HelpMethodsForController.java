package com.example.osbb.controller;

import com.example.osbb.dto.response.ErrorResponseMessages;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HelpMethodsForController {

    public ResponseEntity<?> returnResponse(Object object) {
        return object.getClass().equals(ErrorResponseMessages.class) ?
                ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(object)
                :
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(object);
    }

}
