package com.example.osbb.dto;

import com.example.osbb.dto.messages.ResponseMessages;
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
}
