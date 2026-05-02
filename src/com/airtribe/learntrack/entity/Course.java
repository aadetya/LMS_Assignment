package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.entity.enums.CourseLevel;

public class Course {
    private int id;
    private String courseName;
    private String description;
    private int durationInWeeks;
    private boolean active;
    private String courseCode;
    private boolean enrollmentOpen;
    private int maxSeats;
    private CourseLevel level;
    private Integer trainerId;

    public Course() {
        this.active = true;
        this.enrollmentOpen = false;
        this.maxSeats = 20;
        this.level = CourseLevel.FOUNDATION;
    }

    public Course(int id, String courseName, String description, int durationInWeeks, boolean active, String courseCode, boolean enrollmentOpen, int maxSeats, CourseLevel level, Integer trainerId) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.durationInWeeks = durationInWeeks;
        this.active = active;
        this.courseCode = courseCode;
        this.enrollmentOpen = enrollmentOpen;
        this.maxSeats = maxSeats;
        this.level = level;
        this.trainerId = trainerId;
    }

    public Course(String courseName, int durationInWeeks) {
        this();
        this.courseName = courseName;
        this.durationInWeeks = durationInWeeks;
    }

    public Course(String courseName, int durationInWeeks, int maxSeats) {
        this(courseName, durationInWeeks);
        this.maxSeats = maxSeats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationInWeeks() {
        return durationInWeeks;
    }

    public void setDurationInWeeks(int durationInWeeks) {
        this.durationInWeeks = durationInWeeks;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public boolean isEnrollmentOpen() {
        return enrollmentOpen;
    }

    public void setEnrollmentOpen(boolean enrollmentOpen) {
        this.enrollmentOpen = enrollmentOpen;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public String getShortSummary() {
        return "Course #" + id + " | " + courseCode + " | " + courseName + " | " + getOperationalStatus();
    }

    public String getOperationalStatus() {
        if (!active) {
            return "Inactive";
        }
        if (enrollmentOpen) {
            return "Active + Enrollment Open";
        }
        return "Active + Enrollment Closed";
    }

    public boolean hasTrainerAssigned() {
        return trainerId != null && trainerId.intValue() > 0;
    }

    public String toString() {
        return getShortSummary() + " | Seats: " + maxSeats + " | Level: " + (level == null ? "" : level.getDisplayText());
    }
}
