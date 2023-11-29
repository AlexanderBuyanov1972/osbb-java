package com.example.osbb.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
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
