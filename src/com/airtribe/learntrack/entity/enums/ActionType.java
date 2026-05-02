package com.airtribe.learntrack.entity.enums;

public enum ActionType {
    STUDENT_CREATED,
    STUDENT_UPDATED,
    STUDENT_DEACTIVATED,
    STUDENT_REACTIVATED,
    TRAINER_CREATED,
    TRAINER_UPDATED,
    TRAINER_DEACTIVATED,
    TRAINER_REACTIVATED,
    COURSE_CREATED,
    COURSE_UPDATED,
    COURSE_DEACTIVATED,
    COURSE_REACTIVATED,
    ENROLLMENT_OPENED,
    ENROLLMENT_CLOSED,
    TRAINER_ASSIGNED,
    TRAINER_UNASSIGNED,
    ENROLLMENT_ACCEPTED,
    ENROLLMENT_WAITLISTED,
    ENROLLMENT_PROMOTED,
    ENROLLMENT_COMPLETED,
    ENROLLMENT_CANCELLED,
    SEED_DATA_LOADED,
    GUIDED_DEMO_RAN;

    public String getDisplayText() {
        switch (this) {
            case STUDENT_CREATED:
                return "Student created";
            case STUDENT_UPDATED:
                return "Student updated";
            case STUDENT_DEACTIVATED:
                return "Student safely deactivated";
            case STUDENT_REACTIVATED:
                return "Student reactivated";
            case TRAINER_CREATED:
                return "Trainer created";
            case TRAINER_UPDATED:
                return "Trainer updated";
            case TRAINER_DEACTIVATED:
                return "Trainer safely deactivated";
            case TRAINER_REACTIVATED:
                return "Trainer reactivated";
            case COURSE_CREATED:
                return "Course created";
            case COURSE_UPDATED:
                return "Course updated";
            case COURSE_DEACTIVATED:
                return "Course safely deactivated";
            case COURSE_REACTIVATED:
                return "Course reactivated";
            case ENROLLMENT_OPENED:
                return "Enrollment window opened";
            case ENROLLMENT_CLOSED:
                return "Enrollment window closed";
            case TRAINER_ASSIGNED:
                return "Trainer assigned";
            case TRAINER_UNASSIGNED:
                return "Trainer unassigned";
            case ENROLLMENT_ACCEPTED:
                return "Enrollment accepted";
            case ENROLLMENT_WAITLISTED:
                return "Enrollment waitlisted";
            case ENROLLMENT_PROMOTED:
                return "Waitlist learner promoted";
            case ENROLLMENT_COMPLETED:
                return "Enrollment completed";
            case ENROLLMENT_CANCELLED:
                return "Enrollment cancelled";
            case SEED_DATA_LOADED:
                return "Seed data loaded";
            case GUIDED_DEMO_RAN:
                return "Guided demo ran";
            default:
                return "Unknown action";
        }
    }
}
