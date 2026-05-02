package com.airtribe.learntrack.util;

public final class IdGenerator {
    private static int studentIdCounter = 1000;
    private static int trainerIdCounter = 500;
    private static int courseIdCounter = 2000;
    private static int enrollmentIdCounter = 3000;
    private static int actionLogIdCounter = 8000;
    private static int receiptIdCounter = 9000;

    private IdGenerator() {
    }

    public static int getNextStudentId() {
        studentIdCounter++;
        return studentIdCounter;
    }

    public static int getNextTrainerId() {
        trainerIdCounter++;
        return trainerIdCounter;
    }

    public static int getNextCourseId() {
        courseIdCounter++;
        return courseIdCounter;
    }

    public static int getNextEnrollmentId() {
        enrollmentIdCounter++;
        return enrollmentIdCounter;
    }

    public static int getNextActionLogId() {
        actionLogIdCounter++;
        return actionLogIdCounter;
    }

    public static int getNextReceiptId() {
        receiptIdCounter++;
        return receiptIdCounter;
    }
}
