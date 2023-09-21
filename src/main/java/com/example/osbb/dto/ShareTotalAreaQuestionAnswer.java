package com.example.osbb.dto;

import com.example.osbb.enums.TypeOfAnswer;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShareTotalAreaQuestionAnswer {
    private String question;
    private TypeOfAnswer answer;
    private Double shareTotalArea;
}
