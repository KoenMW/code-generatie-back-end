package com.Inholland.NovaBank.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testConstructor() {
        User actualUser = new User("First Name", "Last Name", "janedoe", "1234567", "henk@gmail.com", Role.ROLE_USER, 1, 1, true);
        assertNotNull(actualUser);
    }
    @Test
    void testConstructor2() {
        User actualUser = new User();
        assertNotNull(actualUser);
    }
}
