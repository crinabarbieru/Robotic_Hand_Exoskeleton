package com.exoapp.controllers;

import com.exoapp.models.dto.ProgressTrackerDTO;
import com.exoapp.models.dto.SupervisorStatsDTO;
import com.exoapp.models.dto.UserInformationDTO;
import com.exoapp.services.SupervisorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from React frontend
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SupervisorController {

    private final SupervisorService supervisorService = new SupervisorService();


    /**
     * API endpoint for getting all patients of the supervisor
     * @param supervisor_id
     * @return list of UserInformationDTO's or error message
     */
    @GetMapping("/supervisor/{supervisor_id}/patients")
    public ResponseEntity<List<UserInformationDTO>> getAllUsers(@PathVariable Long supervisor_id) {
        try {
            List<UserInformationDTO> myPatients = supervisorService.getMyPatients(supervisor_id);
            if (myPatients != null)
                return ResponseEntity.ok(myPatients);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Api endpoint for getting the progress of all patients of the supervisor
     * @param supervisor_id
     * @return list of ProgressTrackerDTO or error message
     */
    @GetMapping("/supervisor/{supervisor_id}/patients/progress")
    public ResponseEntity<List<ProgressTrackerDTO>> getAllProgressTrackers(@PathVariable Long supervisor_id) {
        try{
            List<ProgressTrackerDTO> myPatientsProgress = supervisorService.getPatientsProgress(supervisor_id);
            if(myPatientsProgress != null)
                return ResponseEntity.ok(myPatientsProgress);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * API endpoint to get rehabilitation stats about the patients
     * @param supervisor_id
     * @return SupervisorStatsDTO or error message
     */
    @GetMapping("/supervisor/{supervisor_id}/stats")
    public ResponseEntity<SupervisorStatsDTO> getSupervisorStats(@PathVariable Long supervisor_id) {
        try{
            SupervisorStatsDTO supervisorStatsDTO = supervisorService.getSupervisorStats(supervisor_id);
            if (supervisorStatsDTO != null)
                return ResponseEntity.ok(supervisorStatsDTO);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

}
