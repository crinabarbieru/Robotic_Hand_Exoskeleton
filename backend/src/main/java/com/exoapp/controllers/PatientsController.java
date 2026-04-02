package com.exoapp.controllers;

import com.exoapp.models.dto.*;
import com.exoapp.models.exceptions.StrokeTypeMismatchException;
import com.exoapp.services.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collections;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")  // Allow requests from React frontend
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PatientsController {

    private final PatientService patientService = new PatientService();

    /**
     * API endpoint for HTTP GET request, returns user information as UserinformationDTO object
     * @param userId
     * @return
     */
    @GetMapping("/patients/{userId}")
    public ResponseEntity<UserInformationDTO> getUserInfo(@PathVariable Long userId) {
        try {
            UserInformationDTO userInfo = patientService.getUserInformation(userId);
            if (userInfo != null)
                return ResponseEntity.ok(userInfo);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * API endpoint for GET request, returns the user's rehabilitation progress
     * @param userId
     * @return ProgressTrackerDTO object
     */
    @GetMapping("/patients/{userId}/progress")
    public ResponseEntity<ProgressTrackerDTO> getProgress(@PathVariable Long userId) {
        System.out.println("GETTING PROGRESS FOR ID: " + userId);
        try{
            ProgressTrackerDTO progress = patientService.getProgress(userId);
            return ResponseEntity.ok(progress);
        }catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * API endpoint for registering a new patient
     * @param dto
     * @return confirmation or error message
     */
    @PostMapping("/patients")
    public ResponseEntity<?> addPatient(@Valid @RequestBody AddPatientDTO dto) {
        try {
            patientService.addPatient(dto);
            return ResponseEntity.ok()
                    .body(Collections.singletonMap("message", "Patient added successfully"));
        } catch (IllegalArgumentException | StrokeTypeMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message","An error occurred while adding this patient"));
        }
    }

    /**
     * API endpoint for deletion of a patient from the system
     * @param user_id
     * @return confirmation or error message
     */
    @DeleteMapping("/patients/{user_id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long user_id) {
        try {
            patientService.deleteUser(user_id);
            return ResponseEntity.ok().body(
                    Collections.singletonMap("message", "Patient deleted successfully"));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message","An error occurred while deleting this patient"));
        }
    }

    /**
     * API endpoint for editing patient's information
     * @param user_id
     * @param userInformationDTO
     * @return confirmation or error message
     */
    @PutMapping("/patients/{user_id}")
    public ResponseEntity<?> editPatient(@Valid @PathVariable Long user_id, @Valid @RequestBody UserInformationDTO userInformationDTO)
    {
        try {
            patientService.editUserInformation(user_id, userInformationDTO);
            return ResponseEntity.ok().body(
                    Collections.singletonMap("message", "Patient updated successfully"));
        }catch (IllegalArgumentException | StrokeTypeMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message","An error occurred while editing this patient"));
        }
    }

    /**
     * API endpoint for registering a new exercise session
     * @param exerciseSessionDTO
     * @return confirmation or error message
     */
    @PostMapping("/patients/{userId}/exercise")
    public ResponseEntity<?> addExerciseSession(@Valid @RequestBody ExerciseSessionDTO exerciseSessionDTO)
    {
            try{
                patientService.addExerciseSession(exerciseSessionDTO);
                return ResponseEntity.ok().body("Exercise session recorded successfully");
            } catch (SQLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message",e.getMessage()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message","An error occurred, could not record exercise session"));
            }
    }

    /**
     * API endpoint for recording new Passive Mode score
     * @param userId
     * @param score
     * @return confirmation or error message
     */
    @PostMapping("/patients/{userId}/passive-mode/{score}")
    public ResponseEntity<?> addPassiveSession(@PathVariable @NotNull @Positive Long userId, @PathVariable @NotNull Integer score)
    {
        try{
            patientService.addPassiveSession(userId, score);
            return ResponseEntity.ok().body("Exercise session recorded successfully");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message","An error occurred, could not record exercise session"));
        }
    }

}
