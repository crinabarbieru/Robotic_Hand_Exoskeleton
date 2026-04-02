package com.exoapp.services;

import com.exoapp.models.dto.ExerciseSessionDTO;
import com.exoapp.models.dto.UserInformationDTO;
import com.exoapp.models.exceptions.StrokeTypeMismatchException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PatientServiceTest {
    private final PatientService patientService = new PatientService();

    @Test
    void validateDates_ValidDates_NoExceptionThrown() {
        LocalDate dob = LocalDate.now().minusYears(30);
        LocalDate strokeDate = LocalDate.now().minusYears(1);
        LocalDate rehabDate = LocalDate.now().minusMonths(6);

        assertDoesNotThrow(() -> patientService.validateDates(dob, strokeDate, rehabDate));
    }

    @Test
    void validateDates_FutureBirthDate_ThrowsException() {
        LocalDate dob = LocalDate.now().plusDays(1);
        LocalDate strokeDate = LocalDate.now().minusYears(1);
        LocalDate rehabDate = LocalDate.now().minusMonths(6);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.validateDates(dob, strokeDate, rehabDate));
        assertEquals("Date of birth must be a past date", exception.getMessage());
    }

    @Test
    void validateDates_FutureStrokeDate_ThrowsException() {
        LocalDate dob = LocalDate.now().minusYears(30);
        LocalDate strokeDate = LocalDate.now().plusDays(1);
        LocalDate rehabDate = LocalDate.now().minusMonths(6);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.validateDates(dob, strokeDate, rehabDate));
        assertEquals("Stroke date must be a past date", exception.getMessage());
    }
    @Test
    void validateDates_FutureRehabDate_ThrowsException() {
        LocalDate dob = LocalDate.now().minusYears(30);
        LocalDate strokeDate = LocalDate.now().minusYears(1);
        LocalDate rehabDate = LocalDate.now().plusDays(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.validateDates(dob, strokeDate, rehabDate));
        assertEquals("Rehabilitation start date must be a past date", exception.getMessage());
    }

    @Test
    void validateDates_PatientUnder18_ThrowsException() {
        LocalDate dob = LocalDate.now().minusYears(17);
        LocalDate strokeDate = LocalDate.now().minusYears(1);
        LocalDate rehabDate = LocalDate.now().minusMonths(6);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.validateDates(dob, strokeDate, rehabDate)
        );
        assertEquals("Patient must be at least 18 years old", exception.getMessage());
    }

    @Test
    void validateDates_BirthAfterStroke_ThrowsException() {
        LocalDate dob = LocalDate.now().minusYears(30);
        LocalDate strokeDate = LocalDate.now().minusYears(31);
        LocalDate rehabDate = LocalDate.now().minusYears(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.validateDates(dob, strokeDate, rehabDate)
        );
        assertEquals("Date of birth must be before the stroke occurrence date", exception.getMessage());
    }
    @Test
    void validateDates_BirthAfterRehab_ThrowsException() {
        LocalDate dob = LocalDate.now().minusYears(30);
        LocalDate strokeDate = LocalDate.now().minusMonths(10);
        LocalDate rehabDate = LocalDate.now().minusYears(31);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.validateDates(dob, strokeDate, rehabDate)
        );
        assertEquals("Date of birth must be before the rehabilitation start date", exception.getMessage());
    }
    @Test
    void validateDates_StrokeAfterRehab_ThrowsException() {
        LocalDate dob = LocalDate.now().minusYears(30);
        LocalDate strokeDate = LocalDate.now().minusMonths(6);
        LocalDate rehabDate = LocalDate.now().minusYears(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.validateDates(dob, strokeDate, rehabDate)
        );
        assertEquals("The stroke occurrence date must be before the rehabilitation start date", exception.getMessage());
    }

    @Test
    void editUserInformation_IdMismatch_ThrowsException() {
        UserInformationDTO dto = new UserInformationDTO();
        dto.setId(1L);
        Long incorrectId = 2L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.editUserInformation(incorrectId, dto)
        );
        assertEquals("The id of the user does not match the user information", exception.getMessage());
    }

    @Test
    void addExerciseSession_FutureDate_ThrowsException() {
        ExerciseSessionDTO dto = new ExerciseSessionDTO();
        LocalDate date = LocalDate.now().plusDays(1);
        dto.setDate(date);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.addExerciseSession(dto));
        assertEquals("The date is not in the past", exception.getMessage());
    }

    @Test
    void addExerciseSession_NegativeDuration_ThrowsException() {
        ExerciseSessionDTO dto = new ExerciseSessionDTO();
        LocalDate date = LocalDate.now().minusDays(1);
        Float duration = -1f;
        dto.setDate(date);
        dto.setDuration(duration);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.addExerciseSession(dto));
        assertEquals("The duration cannot be negative", exception.getMessage());
    }

    @Test
    void validateStrokeType_Ischemic_NoExceptionThrown()
    {
        assertDoesNotThrow(() -> patientService.validateStrokeType("Ischemic", "Lacunar") );
    }

    @Test
    void validateStrokeType_Hemorrhagic_NoExceptionThrown()
    {
        assertDoesNotThrow(() -> patientService.validateStrokeType("Hemorrhagic", "Intracerebral") );
    }

    @Test
    void validateStrokeType_IncorrectType_ThrowsException()
    {
        String type = "Other";
        String subtype = "Cardioembolic";
        StrokeTypeMismatchException exception = assertThrows(StrokeTypeMismatchException.class,
                () -> patientService.validateStrokeType(type, subtype));
        assertEquals("Incorrect stroke type", exception.getMessage());
    }

    @Test
    void validateStrokeType_HemorrhagicMismatch1_ThrowsException()
    {
        String type = "Hemorrhagic";
        String subtype = "Cardioembolic";
        StrokeTypeMismatchException exception = assertThrows(StrokeTypeMismatchException.class,
                () -> patientService.validateStrokeType(type, subtype));
        assertEquals("Incorrect subtype for Hemorrhagic stroke", exception.getMessage());
    }

    @Test
    void validateStrokeType_HemorrhagicMismatch2_ThrowsException()
    {
        String type = "Hemorrhagic";
        String subtype = "Lacunar";
        StrokeTypeMismatchException exception = assertThrows(StrokeTypeMismatchException.class,
                () -> patientService.validateStrokeType(type, subtype));
        assertEquals("Incorrect subtype for Hemorrhagic stroke", exception.getMessage());
    }

    @Test
    void validateStrokeType_HemorrhagicMismatch3_ThrowsException()
    {
        String type = "Hemorrhagic";
        String subtype = "Large artery atherosclerosis";
        StrokeTypeMismatchException exception = assertThrows(StrokeTypeMismatchException.class,
                () -> patientService.validateStrokeType(type, subtype));
        assertEquals("Incorrect subtype for Hemorrhagic stroke", exception.getMessage());
    }

    @Test
    void validateStrokeType_IschemicMismatch1_ThrowsException()
    {
        String type = "Ischemic";
        String subtype = "Subarachnoid";
        StrokeTypeMismatchException exception = assertThrows(StrokeTypeMismatchException.class,
                () -> patientService.validateStrokeType(type, subtype));
        assertEquals("Incorrect subtype for Ischemic stroke", exception.getMessage());
    }

    @Test
    void validateStrokeType_IschemicMismatch2_ThrowsException()
    {
        String type = "Ischemic";
        String subtype = "Intracerebral";
        StrokeTypeMismatchException exception = assertThrows(StrokeTypeMismatchException.class,
                () -> patientService.validateStrokeType(type, subtype));
        assertEquals("Incorrect subtype for Ischemic stroke", exception.getMessage());
    }

}
