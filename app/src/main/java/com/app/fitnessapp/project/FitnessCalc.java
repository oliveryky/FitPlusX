package com.app.fitnessapp.project;

public class FitnessCalc {

    private static final double KG_LB_RATIO = .45;
    private static final double METER_IN_RATIO = .025;
    private static final double CM_IN_RATIO = 2.5;
    private static final int CALORIE_LB_RATIO = 3500;
    private static final int DAYS_PER_WEEK = 7;

    public static double getBMI(double weight, double height) {
        double x =  (weight * KG_LB_RATIO) / Math.pow(height * METER_IN_RATIO, 2);
        return x;
    }

    public static int calculateBMR(double weight, double height, int age, String sex, double activityLevel) {
        double bmr = ((4.536 * weight) + (15.88 * height) - (5 * age));

        if(sex.equalsIgnoreCase("male")) {
            bmr += 5;
        }else {
            bmr -= 161;
        }
        bmr *= activityLevel;
        return (int) bmr;
    }

    public static int calculateDailyCalorieIntake(double goal, String sex, double weight, double height, int age, double activityLevel){
        double dailyIntake = calculateBMR(weight, height, age, sex, activityLevel) + ((goal * CALORIE_LB_RATIO) / DAYS_PER_WEEK);
        return dailyIntake < 0.0 ? (sex.equals("Male") ? 1200 : 1000) : (int) dailyIntake;
    }
}
