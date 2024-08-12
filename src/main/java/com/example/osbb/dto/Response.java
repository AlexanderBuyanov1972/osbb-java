package com.example.osbb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private Object data;
    private List<String> messages;

    public Response(List<String> messages) {
        this.messages = messages;
    }
}
