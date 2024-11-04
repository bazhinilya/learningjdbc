package com.example.models;

import java.time.LocalDate;
import java.time.Period;

public class Employee {
    private String fullName;
    private LocalDate birthDate;
    private String gender;

    public Employee(String fullName, LocalDate birthDate, String gender) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public int calculateAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %d",
                fullName, birthDate, gender, calculateAge());
    }
}