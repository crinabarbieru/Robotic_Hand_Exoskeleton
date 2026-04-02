package com.exoapp.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    void checkPasswordChange_NoChange(){
        User user = new User();
        LocalDate lastPassChange = LocalDate.now().minusMonths(2);
        user.setLastPasswordChange(lastPassChange);
        user.setPasswordChanged(true);

        boolean result = user.shouldChangePassword();
        assertFalse(result);

    }

    @Test
    void checkPasswordChange_NeedsChange_NeverChanged(){
        User user = new User();
        user.setPasswordChanged(false);
        boolean result = user.shouldChangePassword();
        assertTrue(result);
    }

    @Test
    void checkPasswordChange_NeedsChange_ChangedTooLongAgo(){
        User user = new User();
        user.setPasswordChanged(true);
        LocalDate lastPassChange = LocalDate.now().minusDays(92);
        user.setLastPasswordChange(lastPassChange);
        boolean result = user.shouldChangePassword();
        assertTrue(result);
    }
}
