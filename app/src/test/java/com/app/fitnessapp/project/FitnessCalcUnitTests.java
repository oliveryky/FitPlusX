package com.app.fitnessapp.project;

import org.junit.Test;

import static org.junit.Assert.*;

public class FitnessCalcUnitTests {

    // use the "Test User" in the singleton class to test

    @Test
    public void getBMI_test(){
        Singleton.setPath(Utils.getDirPath());
        Singleton.getInstance();
        assertEquals(FitnessCalc.getBMI(), 22.22, 0.05);
    }

    @Test
    public void calculateBMR_test (){
        Singleton.setPath(Utils.getDirPath());
        Singleton.getInstance();
        assertEquals(FitnessCalc.calculateBMR(), 3044);
    }

    @Test
    public void dailyCalorie_test(){
        Singleton.setPath(Utils.getDirPath());
        Singleton.getInstance();
        assertEquals(FitnessCalc.calculateDailyCalorieIntake(), 4044);
    }

}
