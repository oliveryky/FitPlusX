package com.app.fitplusx.project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserProfileUnitTests {

    // Not much logic in this class, so only testing the init of the class
    @Test
    public void initialization_is_correct(){
        UserProfile up = new UserProfile();
        assertEquals(up.name,  "");
        assertEquals(up.birthday, "");
        assertEquals(up.sex, "");
        assertEquals(up.location, "");
        assertEquals(up.imageUri, "");
        assertEquals(up.height, 0, 0.05);
        assertEquals(up.weight,0, 0.05);
        assertEquals(up.activityLevel, 0, 0.05);
        assertEquals(up.dailyCaloricIntake, 0);
    }

}
