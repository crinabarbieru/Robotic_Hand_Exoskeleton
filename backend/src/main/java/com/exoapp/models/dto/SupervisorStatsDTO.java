package com.exoapp.models.dto;

import java.util.HashMap;
import java.util.Map;

public class SupervisorStatsDTO {
    private int totalPatients;
    private int sessionsThisWeek;
    private float totalMinutes;
    private float avgSessionDuration;
    private Map<String, Integer> ageDistribution;

    public SupervisorStatsDTO() {
        totalPatients = 0;
        sessionsThisWeek = 0;
        totalMinutes = 0;
        avgSessionDuration = 0;
        ageDistribution = null;
    }

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    public int getSessionsThisWeek() {
        return sessionsThisWeek;
    }

    public void setSessionsThisWeek(int sessionsThisWeek) {
        this.sessionsThisWeek = sessionsThisWeek;
    }

    public float getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(float totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public float getAvgSessionDuration() {
        return avgSessionDuration;
    }

    public void setAvgSessionDuration(float avgSessionDuration) {
        this.avgSessionDuration = avgSessionDuration;
    }

    public Map<String, Integer> getAgeDistribution() {
        return ageDistribution;
    }

    public void setAgeDistribution(Map<String, Integer> ageDistribution) {
        this.ageDistribution = ageDistribution;
    }
}
