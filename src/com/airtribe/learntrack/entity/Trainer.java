package com.airtribe.learntrack.entity;

import java.time.LocalDate;

public class Trainer extends Person {
    private String expertise;
    private boolean active;
    private LocalDate joinedOn;

    public Trainer() {
        this.active = true;
        this.joinedOn = LocalDate.now();
    }

    public Trainer(int id, String firstName, String lastName, String email, String expertise, boolean active, LocalDate joinedOn) {
        super(id, firstName, lastName, email);
        this.expertise = expertise;
        this.active = active;
        this.joinedOn = joinedOn;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(LocalDate joinedOn) {
        this.joinedOn = joinedOn;
    }

    public String getDisplayName() {
        String state = active ? "Active" : "Inactive";
        return "Trainer #" + getId() + " - " + getFullName() + " | Expertise: " + expertise + " | " + state;
    }

    public String getTrainerCard() {
        return getDisplayName() + " | Contact: " + getContactLine() + " | Joined: " + joinedOn;
    }

    public String toString() {
        return getTrainerCard();
    }
}
