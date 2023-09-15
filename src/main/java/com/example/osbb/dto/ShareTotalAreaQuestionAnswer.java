package com.example.osbb.dto;

import com.example.osbb.enums.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareTotalAreaQuestionAnswer {
    private String question;
    private Answer answer;
    private Double shareTotalArea;
}
