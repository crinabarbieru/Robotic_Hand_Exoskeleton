package com.exoapp.models.dto;

import java.time.LocalDate;
import java.util.Map;

public class ProgressTrackerDTO {
    private Long id;
    private String name;
    private LocalDate strokeDate;
    private LocalDate rehabStartDate;
    private Map<LocalDate, Float> sessions;
    Map<LocalDate, Integer> passiveScores;

    public ProgressTrackerDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStrokeDate() {
        return strokeDate;
    }

    public void setStrokeDate(LocalDate strokeDate) {
        this.strokeDate = strokeDate;
    }

    public LocalDate getRehabStartDate() {
        return rehabStartDate;
    }

    public void setRehabStartDate(LocalDate rehabStartDate) {
        this.rehabStartDate = rehabStartDate;
    }

    public Map<LocalDate, Float> getSessions() {
        return sessions;
    }

    public void setSessions(Map<LocalDate, Float> sessions) {
        this.sessions = sessions;
    }

    public Map<LocalDate, Integer> getPassiveScores() {
        return passiveScores;
    }

    public void setPassiveScores(Map<LocalDate, Integer> passiveScores) {
        this.passiveScores = passiveScores;
    }
}

