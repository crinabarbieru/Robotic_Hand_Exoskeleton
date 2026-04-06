package com.exoapp.services;

import com.exoapp.database.UserDBOp;
import com.exoapp.models.Role;
import com.exoapp.models.User;
import com.exoapp.models.exceptions.InvalidPasswordException;
import com.exoapp.models.login.LoginResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final UserDBOp userDBOp = new UserDBOp();

    public static void registerUser(String username, String password) throws Exception {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, hashedPassword, Role.ROLE_PATIENT);
        userDBOp.insertUser(user);
    }

    public void deleteUser(Long id) throws SQLException
    {
        userDBOp.deleteUser(id);
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws Exception {
            User user = userDBOp.findByUsername(username);
            validatePassChange(user.getPassword(), oldPassword, newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
            userDBOp.changePassword(user.getId(), user.getPassword());

    }

    public LoginResponse validateUser(String username, String password) throws SQLException{
        User user = userDBOp.findByUsername(username);
        if (passwordEncoder.matches(password, user.getPassword())) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(user.getId());
            loginResponse.setUsername(user.getUsername());
            loginResponse.setRole(user.getRole().getCode());
            loginResponse.setNeedsPasswordChange(!user.isPasswordChanged());
            return loginResponse;
        }
        throw new UsernameNotFoundException("Invalid credentials");
    }

    public void validatePassChange(String actualPassword, String oldPassword, String newPassword) throws InvalidPasswordException{
        if(!passwordEncoder.matches(oldPassword, actualPassword)) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
        if(passwordEncoder.matches(newPassword, actualPassword)) {
            throw new InvalidPasswordException("New password cannot match the old one");
        }
        if (newPassword.length() < 8) {
            throw new InvalidPasswordException("New password must be at least 8 characters long");
        }
    }


}

