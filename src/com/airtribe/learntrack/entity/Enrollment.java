package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.entity.enums.EnrollmentStatus;
import java.time.LocalDate;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private LocalDate enrollmentDate;
    private EnrollmentStatus status;
    private LocalDate lastStatusChangedOn;
    private String statusNote;

    public Enrollment() {
        this.enrollmentDate = LocalDate.now();
        this.status = EnrollmentStatus.ACTIVE;
        this.lastStatusChangedOn = LocalDate.now();
        this.statusNote = "Created";
    }

    public Enrollment(int id, int studentId, int courseId, LocalDate enrollmentDate, EnrollmentStatus status, LocalDate lastStatusChangedOn, String statusNote) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.lastStatusChangedOn = lastStatusChangedOn;
        this.statusNote = statusNote;
    }

    public Enrollment(int id, int studentId, int courseId) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = LocalDate.now();
        this.status = EnrollmentStatus.ACTIVE;
        this.lastStatusChangedOn = LocalDate.now();
        this.statusNote = "Created";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDate getLastStatusChangedOn() {
        return lastStatusChangedOn;
    }

    public void setLastStatusChangedOn(LocalDate lastStatusChangedOn) {
        this.lastStatusChangedOn = lastStatusChangedOn;
    }

    public String getStatusNote() {
        return statusNote;
    }

    public void setStatusNote(String statusNote) {
        this.statusNote = statusNote;
    }

    public boolean isOpen() {
        return status != null && status.isOpen();
    }

    public boolean isFinal() {
        return status != null && status.isFinal();
    }

    public void markActive(String note) {
        status = EnrollmentStatus.ACTIVE;
        lastStatusChangedOn = LocalDate.now();
        statusNote = note;
    }

    public void markWaitlisted(String note) {
        status = EnrollmentStatus.WAITLISTED;
        lastStatusChangedOn = LocalDate.now();
        statusNote = note;
    }

    public void markCompleted(String note) {
        status = EnrollmentStatus.COMPLETED;
        lastStatusChangedOn = LocalDate.now();
        statusNote = note;
    }

    public void markCancelled(String note) {
        status = EnrollmentStatus.CANCELLED;
        lastStatusChangedOn = LocalDate.now();
        statusNote = note;
    }

    public String toString() {
        return "Enrollment #" + id + " | Student #" + studentId + " | Course #" + courseId + " | " + status.getDisplayText() + " | " + statusNote;
    }
}
