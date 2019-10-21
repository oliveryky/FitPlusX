package com.app.fitnessapp.project.Repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_data_table")
public class UserDataTable {

    @NonNull // none of these fields can be null
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String birthday;
    private String location;
    private String sex;
    private String imageUri;
    private double height;
    private double weight;
    private double activityLevel;
    private double goal;
    private int dailyCaloricIntake;
    private int age;

    @Ignore
    public UserDataTable() {
        this.userName = "";
        this.password = "";
        this.name = "";
        this.birthday = "";
        this.location = "";
        this.imageUri = "";
        this.sex = "";
        this.height = 0;
        this.weight = 0;
        this.age = 0;
        this.activityLevel = 0;
        this.goal = 0;
        this.dailyCaloricIntake = 0;
    }

    public UserDataTable(@NonNull String userName, @NonNull String password) {
        this();
        this.userName = userName;
        this.password = password;
    }

    // Use a ModelUserData object as the
    @Ignore
    public UserDataTable(@NonNull String birthday, String sex, String location, String imageUri, String password,
                         double height, double weight, double activityLevel, double goal,
                         int dailyCaloricIntake, int age, String userName, String name) {
        this.birthday = birthday;
        this.sex = sex;
        this.location = location;
        this.imageUri = imageUri;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.dailyCaloricIntake = dailyCaloricIntake;
        this.age = age;
        this.userName = userName;
        this.name = name;
        this.password = password;
    }


    @NonNull
    @PrimaryKey
    // userName for the key? crappy but will hold the fort for now
    private String userName;

    @NonNull
    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @NonNull
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @NonNull
    public String getLocation() { return location; }

    public void setLocation(String location) {
        this.location = location;
    }

    @NonNull
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(double activityLevel) {
        this.activityLevel = activityLevel;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public int getDailyCaloricIntake() {
        return dailyCaloricIntake;
    }

    public void setDailyCaloricIntake(int dailyCaloricIntake) {
        this.dailyCaloricIntake = dailyCaloricIntake;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }
}
