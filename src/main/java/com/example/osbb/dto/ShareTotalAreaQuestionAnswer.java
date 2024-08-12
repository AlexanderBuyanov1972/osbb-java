package com.example.osbb.dto;

import com.example.osbb.enums.TypeOfAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareTotalAreaQuestionAnswer {
    private String question;
    private TypeOfAnswer answer;
    // доля в квартире умноженная на общую площадь той же квартиры
    private Double shareTotalArea;

}
