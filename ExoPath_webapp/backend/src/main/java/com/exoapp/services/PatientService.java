package com.exoapp.services;

import com.exoapp.database.PatientDBOp;
import com.exoapp.database.UserDBOp;
import com.exoapp.models.dto.*;
import com.exoapp.models.exceptions.StrokeTypeMismatchException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.exoapp.services.UserService.registerUser;

@Service
public class PatientService {

    private static final UserDBOp userDBOp = new UserDBOp();
    private static final PatientDBOp patientDBOp = new PatientDBOp();

    public void addPatient(AddPatientDTO dto) throws Exception {
        validateDates(dto.getDateOfBirth(), dto.getStrokeDate(), dto.getRehabStartDate());
        validateStrokeType(dto.getStrokeType(), dto.getStrokeSubtype());
        String username = dto.getUsername();
        registerUser(username, dto.getPassword());

        UserInformationDTO userInformationDTO = new UserInformationDTO(dto.getFullName(), dto.getDateOfBirth(), dto.getStrokeType(), dto.getStrokeSubtype(), dto.getStrokeDate(), dto.getRehabStartDate());
        userDBOp.insertUserInformation(userInformationDTO, username);

        userDBOp.insertSupervisorUser(username, dto.getSupervisorId());
    }

    public void editUserInformation(Long id, UserInformationDTO userInformationDTO) throws Exception
    {
        if(!Objects.equals(id, userInformationDTO.getId()))
            throw new IllegalArgumentException("The id of the user does not match the user information");
        validateDates(userInformationDTO.getDateOfBirth(), userInformationDTO.getStrokeDate(), userInformationDTO.getRehabStartDate());
        validateStrokeType(userInformationDTO.getStrokeType(), userInformationDTO.getStrokeSubtype());
        userDBOp.editUserInformation(userInformationDTO);
    }

    public void deleteUser(long userId) throws SQLException{
        userDBOp.deleteUser(userId);
    }

    public UserInformationDTO getUserInformation(Long userId) throws SQLException {
        return userDBOp.getUserInformation(userId);
    }

    public ProgressTrackerDTO getProgress(Long userId) throws SQLException {
        UserInformationDTO userInformationDTO = getUserInformation(userId);
        ProgressTrackerDTO progressTrackerDTO = new ProgressTrackerDTO();
        progressTrackerDTO.setId(userId);
        progressTrackerDTO.setName(userInformationDTO.getName());
        progressTrackerDTO.setStrokeDate(userInformationDTO.getStrokeDate());
        progressTrackerDTO.setRehabStartDate(userInformationDTO.getRehabStartDate());
        Map<LocalDate, Float> sessions = patientDBOp.getSessions(userId);
        Map<LocalDate, Integer> passiveScores = patientDBOp.getPassiveScores(userId);
        progressTrackerDTO.setSessions(sessions);
        progressTrackerDTO.setPassiveScores(passiveScores);
        return progressTrackerDTO;
    }
    public void addExerciseSession(ExerciseSessionDTO exerciseSessionDTO) throws Exception {
        if(exerciseSessionDTO.getDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("The date is not in the past");
        if(exerciseSessionDTO.getDuration() < 0)
            throw new IllegalArgumentException("The duration cannot be negative");
        patientDBOp.addExerciseSession(exerciseSessionDTO.getUserId(), exerciseSessionDTO.getDate(), exerciseSessionDTO.getDuration());
    }

    public void validateDates(LocalDate dateOfBirth, LocalDate strokeDate, LocalDate rehabStartDate) throws IllegalArgumentException {
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date of birth must be a past date");
        if (strokeDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Stroke date must be a past date");
        if (rehabStartDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Rehabilitation start date must be a past date");
        if (Period.between(dateOfBirth, LocalDate.now()).getYears() < 18)
            throw new IllegalArgumentException("Patient must be at least 18 years old");
        if (dateOfBirth.isAfter(strokeDate))
            throw new IllegalArgumentException("Date of birth must be before the stroke occurrence date");
        if (dateOfBirth.isAfter(rehabStartDate))
            throw new IllegalArgumentException("Date of birth must be before the rehabilitation start date");
        if (strokeDate.isAfter(rehabStartDate))
            throw new IllegalArgumentException("The stroke occurrence date must be before the rehabilitation start date");
    }

    public void validateStrokeType(String type, String subtype) throws StrokeTypeMismatchException
    {
        List<String> ischemicSubtypes = List.of("Lacunar", "Cardioembolic", "Large artery atherosclerosis", "Other");
        List<String> hemorrhagicSubtypes = List.of("Subarachnoid", "Intracerebral");

        if(!(type.equals("Ischemic") || type.equals("Hemorrhagic")))
            throw new StrokeTypeMismatchException("Incorrect stroke type");
        if(type.equals("Ischemic") && !ischemicSubtypes.contains(subtype))
            throw new StrokeTypeMismatchException("Incorrect subtype for Ischemic stroke");
        if(type.equals("Hemorrhagic") && !hemorrhagicSubtypes.contains(subtype))
            throw new StrokeTypeMismatchException("Incorrect subtype for Hemorrhagic stroke");
    }

    public void addPassiveSession(Long userId, Integer score) throws  SQLException{
        LocalDate date = LocalDate.now();
        patientDBOp.addPassiveSession(userId, date, score);
    }
}
