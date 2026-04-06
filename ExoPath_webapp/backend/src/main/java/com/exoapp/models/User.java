package com.exoapp.models;

import org.springframework.security.core.GrantedAuthority;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;

public class User{
    private Long id;

    private String username;
    private String password;

    private Role role;

    private boolean passwordChanged;
    private LocalDate lastPasswordChange;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.ROLE_PATIENT;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String username, String password, Role role, boolean passwordChanged, LocalDate lastPasswordChange) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.passwordChanged = passwordChanged;
        this.lastPasswordChange = lastPasswordChange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }


    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public LocalDate getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(LocalDate lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public boolean shouldChangePassword()
    {
        LocalDate today = LocalDate.now();
        if(!passwordChanged || lastPasswordChange==null)
            return true;
        long daysBetween = ChronoUnit.DAYS.between(lastPasswordChange, today);
        return daysBetween>90;
    }
}
