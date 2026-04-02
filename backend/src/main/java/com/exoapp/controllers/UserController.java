package com.exoapp.controllers;

import com.exoapp.models.dto.PasswordChangeDTO;
import com.exoapp.models.exceptions.InvalidPasswordException;
import com.exoapp.models.login.LoginRequest;
import com.exoapp.models.login.LoginResponse;
import com.exoapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collections;


@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService = new UserService();

    /**
     * API endpoint for user login
     * @param loginRequest
     * @return LoginResponse if credentials are valid, error message otherwise
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        try {
            LoginResponse loginResponse = userService.validateUser(username, password);
                return ResponseEntity.ok(loginResponse);
            }catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    /**
     * API endpoint for changing the user's password
     * @param passwordChangeDTO
     * @return confirmation or error message
     */
    @PostMapping("/auth/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            userService.changePassword(passwordChangeDTO.getUsername(), passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
            return ResponseEntity.ok().body("Password changed");
        } catch (InvalidPasswordException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "An error occurred while attempting to change password"));
        }

    }
}
