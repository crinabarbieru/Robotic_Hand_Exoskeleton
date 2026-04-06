package com.exoapp.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AddPatientDTO {
    @NotBlank(message = "Username is required")
    @Size(min=3, max=30, message = "Username must be 3-30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Stroke type is required")
    private String strokeType;

    @NotBlank(message = "Stroke subtype is required")
    private String strokeSubtype;

    @NotNull(message = "Stroke date is required")
    private LocalDate strokeDate;

    @NotNull(message = "Rehabilitation date is required")
    private LocalDate rehabStartDate;

    @NotNull(message = "Supervisor id is required")
    @Positive(message = "Supervisor ID must be a positive number")
    private Long supervisorId;

    public AddPatientDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public @NotNull(message = "Supervisor id is required") @Positive(message = "Supervisor ID must be a positive number") Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(@NotNull(message = "Supervisor id is required") @Positive(message = "Supervisor ID must be a positive number") Long supervisorId) {
        this.supervisorId = supervisorId;
    }
}
