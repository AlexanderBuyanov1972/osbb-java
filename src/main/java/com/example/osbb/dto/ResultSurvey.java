package com.example.osbb.dto;

import com.example.osbb.enums.TypeOfAnswer;

import java.util.Map;

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

    public ResultSurvey() {
    }

    public ResultSurvey(Double processingPercentageOwner, Long ownerVoted, Long countOwner, Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner, Double processingPercentageArea, Double areaVoted, Double summaArea, Map<String, Map<TypeOfAnswer, Double>> mapVotedArea) {
        this.processingPercentageOwner = processingPercentageOwner;
        this.ownerVoted = ownerVoted;
        this.countOwner = countOwner;
        this.mapVotedOwner = mapVotedOwner;
        this.processingPercentageArea = processingPercentageArea;
        this.areaVoted = areaVoted;
        this.summaArea = summaArea;
        this.mapVotedArea = mapVotedArea;
    }

    public Double getProcessingPercentageOwner() {
        return processingPercentageOwner;
    }

    public Long getOwnerVoted() {
        return ownerVoted;
    }

    public Long getCountOwner() {
        return countOwner;
    }

    public Map<String, Map<TypeOfAnswer, Long>> getMapVotedOwner() {
        return mapVotedOwner;
    }

    public Double getProcessingPercentageArea() {
        return processingPercentageArea;
    }

    public Double getAreaVoted() {
        return areaVoted;
    }

    public Double getSummaArea() {
        return summaArea;
    }

    public Map<String, Map<TypeOfAnswer, Double>> getMapVotedArea() {
        return mapVotedArea;
    }

    public ResultSurvey setProcessingPercentageOwner(Double processingPercentageOwner) {
        this.processingPercentageOwner = processingPercentageOwner;
        return this;
    }

    public ResultSurvey setOwnerVoted(Long ownerVoted) {
        this.ownerVoted = ownerVoted;
        return this;
    }

    public ResultSurvey setCountOwner(Long countOwner) {
        this.countOwner = countOwner;
        return this;
    }

    public ResultSurvey setMapVotedOwner(Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner) {
        this.mapVotedOwner = mapVotedOwner;
        return this;
    }

    public ResultSurvey setProcessingPercentageArea(Double processingPercentageArea) {
        this.processingPercentageArea = processingPercentageArea;
        return this;
    }

    public ResultSurvey setAreaVoted(Double areaVoted) {
        this.areaVoted = areaVoted;
        return this;
    }

    public ResultSurvey setSummaArea(Double summaArea) {
        this.summaArea = summaArea;
        return this;
    }

    public ResultSurvey setMapVotedArea(Map<String, Map<TypeOfAnswer, Double>> mapVotedArea) {
        this.mapVotedArea = mapVotedArea;
        return this;
    }

    @Override
    public String toString() {
        return "ResultSurvey = { processingPercentageOwner = " + processingPercentageOwner +
                ", ownerVoted = " + ownerVoted + ", countOwner = " + countOwner + ", mapVotedOwner = " + mapVotedOwner +
                ", processingPercentageArea = " + processingPercentageArea + ", areaVoted = " + areaVoted +
                ", summaArea = " + summaArea + ", mapVotedArea = " + mapVotedArea + "}";
    }
}
