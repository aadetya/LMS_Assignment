package com.airtribe.learntrack.entity;

import java.time.LocalDate;

public class Student extends Person {
    private String batch;
    private boolean active;
    private String learningGoal;
    private LocalDate joinedOn;

    public Student() {
        this.active = true;
        this.joinedOn = LocalDate.now();
    }

    public Student(int id, String firstName, String lastName, String email, String batch, boolean active, String learningGoal, LocalDate joinedOn) {
        super(id, firstName, lastName, email);
        this.batch = batch;
        this.active = active;
        this.learningGoal = learningGoal;
        this.joinedOn = joinedOn;
    }

    public Student(int id, String firstName, String lastName, String batch, String learningGoal, LocalDate joinedOn) {
        super(id, firstName, lastName, "");
        this.batch = batch;
        this.active = true;
        this.learningGoal = learningGoal;
        this.joinedOn = joinedOn;
    }

    public Student(int id, String firstName, String lastName, String email, String batch) {
        super(id, firstName, lastName, email);
        this.batch = batch;
        this.active = true;
        this.learningGoal = "";
        this.joinedOn = LocalDate.now();
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLearningGoal() {
        return learningGoal;
    }

    public void setLearningGoal(String learningGoal) {
        this.learningGoal = learningGoal;
    }

    public LocalDate getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(LocalDate joinedOn) {
        this.joinedOn = joinedOn;
    }

    public String getDisplayName() {
        String state = active ? "Active" : "Inactive";
        return "Student #" + getId() + " - " + getFullName() + " | Batch: " + batch + " | " + state;
    }

    public String getStudentCard() {
        return getDisplayName() + " | Goal: " + (learningGoal == null ? "" : learningGoal) + " | Joined: " + joinedOn;
    }

    public String toString() {
        return getStudentCard();
    }
}
