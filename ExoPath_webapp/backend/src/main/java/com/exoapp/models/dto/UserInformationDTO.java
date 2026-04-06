package com.exoapp.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UserInformationDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate dateOfBirth;
    @NotBlank
    private String strokeType;
    @NotBlank
    private String strokeSubtype;
    @NotNull
    private LocalDate strokeDate;
    @NotNull
    private LocalDate rehabStartDate;


    public UserInformationDTO() {
        this.id = null;
        this.name = "";
        this.dateOfBirth = null;
        this.strokeType = "";
        this.strokeSubtype = "";
        this.strokeDate = null;
        this.rehabStartDate = null;
    }

    public UserInformationDTO(Long id, String name, LocalDate dateOfBirth, String strokeType, String strokeSubtype, LocalDate strokeDate, LocalDate rehabStartDate) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.strokeType = strokeType;
        this.strokeSubtype = strokeSubtype;
        this.strokeDate = strokeDate;
        this.rehabStartDate = rehabStartDate;
    }

    public UserInformationDTO(String name, LocalDate dateOfBirth, String strokeType, String strokeSubtype, LocalDate strokeDate, LocalDate rehabStartDate) {
        this.id = null;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.strokeType = strokeType;
        this.strokeSubtype = strokeSubtype;
        this.strokeDate = strokeDate;
        this.rehabStartDate = rehabStartDate;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStrokeType() {
        return strokeType;
    }

    public void setStrokeType(String strokeType) {
        this.strokeType = strokeType;
    }

    public String getStrokeSubtype() {
        return strokeSubtype;
    }

    public void setStrokeSubtype(String strokeSubtype) {
        this.strokeSubtype = strokeSubtype;
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
}
