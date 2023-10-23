package com.example.osbb.dto;

import com.example.osbb.enums.TypeOfAnswer;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultQuestionnaire {
    private Double processingPercentageOwner;
    private Long ownerVoted;
    private Long countOwner;
    private Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner;
    private Double processingPercentageArea;
    private Double areaVoted;
    private Double summaArea;
    private Map<String, Map<TypeOfAnswer, Double>> mapVotedArea;
}
