package com.example.osbb.dto;

import com.example.osbb.enums.TypeOfAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultSurvey {
    // owner ----------------
    private Double processingPercentageOwner;
    private Long ownerVoted;
    private Long countOwner;
    private Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner;
    // area -----------------
    private Double processingPercentageArea;
    private Double areaVoted;
    private Double summaArea;
    private Map<String, Map<TypeOfAnswer, Double>> mapVotedArea;

}
