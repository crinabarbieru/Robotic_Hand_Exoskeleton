package com.exoapp.services;

import com.exoapp.models.User;
import com.exoapp.models.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserService userService = new UserService();

    private static String actual;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @BeforeAll
    static void setUp() {
        actual = passwordEncoder.encode("current_password");
    }

    @Test
    void validatePassChange_ValidPassword_NoExceptionThrown(){
        String old = "current_password";
        String newPassword = "newPassword";

        assertDoesNotThrow(() -> userService.validatePassChange(actual, old, newPassword));
    }

    @Test
    void validatePassChange_CurrentOldMismatch_ThrowsException(){
        String old = "current_passwodr";
        String newPassword = "newPassword";
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> userService.validatePassChange(actual, old, newPassword));
        assertEquals("Current password is incorrect", exception.getMessage());
    }

    @Test
    void validatePassChange_CurrentNewMismatch_ThrowsException(){
        String old = "current_password";
        String newPassword = "current_password";

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> userService.validatePassChange(actual, old, newPassword));
        assertEquals("New password cannot match the old one", exception.getMessage());
    }

    @Test
    void validatePassChange_NewPassLength_ThrowsException(){
        String old = "current_password";
        String newPassword = "newPass";

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> userService.validatePassChange(actual, old, newPassword));
        assertEquals("New password must be at least 8 characters long", exception.getMessage());
    }

}
