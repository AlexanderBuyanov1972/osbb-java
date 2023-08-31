package com.example.osbb.dto.messages;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseMessages {
    private List<String> messages;

}
