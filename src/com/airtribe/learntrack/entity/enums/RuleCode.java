package com.airtribe.learntrack.entity.enums;

public enum RuleCode {
    STUDENT_NAME_REQUIRED,
    STUDENT_EMAIL_INVALID,
    STUDENT_EMAIL_UNIQUE,
    COURSE_NAME_REQUIRED,
    COURSE_CODE_UNIQUE,
    COURSE_NAME_UNIQUE_ACTIVE,
    COURSE_INACTIVE,
    COURSE_ENROLLMENT_CLOSED,
    COURSE_CAPACITY_POSITIVE,
    TRAINER_EMAIL_UNIQUE,
    TRAINER_INACTIVE_ASSIGNMENT,
    ENROLL_STUDENT_REQUIRED,
    ENROLL_COURSE_REQUIRED,
    ENROLL_DUPLICATE_OPEN,
    ENROLL_CAPACITY_FULL_WAITLIST,
    ENROLL_FINAL_STATE,
    ENROLL_INACTIVE_STUDENT,
    DEACTIVATE_STUDENT_HAS_OPEN_ENROLLMENTS,
    DEACTIVATE_COURSE_HAS_ACTIVE_ENROLLMENTS,
    DEACTIVATE_TRAINER_ASSIGNED_TO_ACTIVE_COURSE,
    INPUT_POSITIVE_NUMBER_REQUIRED,
    INPUT_MENU_CHOICE_INVALID;

    public String getCode() {
        switch (this) {
            case STUDENT_NAME_REQUIRED:
                return "LT-RULE-STUDENT-001";
            case STUDENT_EMAIL_INVALID:
                return "LT-RULE-STUDENT-002";
            case STUDENT_EMAIL_UNIQUE:
                return "LT-RULE-STUDENT-003";
            case COURSE_NAME_REQUIRED:
                return "LT-RULE-COURSE-001";
            case COURSE_CODE_UNIQUE:
                return "LT-RULE-COURSE-002";
            case COURSE_NAME_UNIQUE_ACTIVE:
                return "LT-RULE-COURSE-003";
            case COURSE_INACTIVE:
                return "LT-RULE-COURSE-004";
            case COURSE_ENROLLMENT_CLOSED:
                return "LT-RULE-COURSE-005";
            case COURSE_CAPACITY_POSITIVE:
                return "LT-RULE-COURSE-006";
            case TRAINER_EMAIL_UNIQUE:
                return "LT-RULE-TRAINER-001";
            case TRAINER_INACTIVE_ASSIGNMENT:
                return "LT-RULE-TRAINER-002";
            case ENROLL_STUDENT_REQUIRED:
                return "LT-RULE-ENROLL-001";
            case ENROLL_COURSE_REQUIRED:
                return "LT-RULE-ENROLL-002";
            case ENROLL_DUPLICATE_OPEN:
                return "LT-RULE-ENROLL-003";
            case ENROLL_CAPACITY_FULL_WAITLIST:
                return "LT-RULE-ENROLL-004";
            case ENROLL_FINAL_STATE:
                return "LT-RULE-ENROLL-005";
            case ENROLL_INACTIVE_STUDENT:
                return "LT-RULE-ENROLL-006";
            case DEACTIVATE_STUDENT_HAS_OPEN_ENROLLMENTS:
                return "LT-RULE-DEACTIVATE-001";
            case DEACTIVATE_COURSE_HAS_ACTIVE_ENROLLMENTS:
                return "LT-RULE-DEACTIVATE-002";
            case DEACTIVATE_TRAINER_ASSIGNED_TO_ACTIVE_COURSE:
                return "LT-RULE-DEACTIVATE-003";
            case INPUT_POSITIVE_NUMBER_REQUIRED:
                return "LT-RULE-INPUT-001";
            case INPUT_MENU_CHOICE_INVALID:
                return "LT-RULE-INPUT-002";
            default:
                return "LT-RULE-UNKNOWN";
        }
    }

    public String getMessage() {
        switch (this) {
            case STUDENT_NAME_REQUIRED:
                return "Student name cannot be blank.";
            case STUDENT_EMAIL_INVALID:
                return "Email must contain '@' and '.'.";
            case STUDENT_EMAIL_UNIQUE:
                return "Student email must be unique.";
            case COURSE_NAME_REQUIRED:
                return "Course name cannot be blank.";
            case COURSE_CODE_UNIQUE:
                return "Course code must be unique.";
            case COURSE_NAME_UNIQUE_ACTIVE:
                return "Active course names should not be duplicated.";
            case COURSE_INACTIVE:
                return "Inactive course cannot accept enrollment.";
            case COURSE_ENROLLMENT_CLOSED:
                return "Closed enrollment window blocks new enrollment.";
            case COURSE_CAPACITY_POSITIVE:
                return "Course capacity must be positive.";
            case TRAINER_EMAIL_UNIQUE:
                return "Trainer email must be unique.";
            case TRAINER_INACTIVE_ASSIGNMENT:
                return "Inactive trainer cannot be assigned to a course.";
            case ENROLL_STUDENT_REQUIRED:
                return "Student must exist before enrollment.";
            case ENROLL_COURSE_REQUIRED:
                return "Course must exist before enrollment.";
            case ENROLL_DUPLICATE_OPEN:
                return "Duplicate open enrollment is not allowed.";
            case ENROLL_CAPACITY_FULL_WAITLIST:
                return "Full course sends learner to waitlist.";
            case ENROLL_FINAL_STATE:
                return "Completed and cancelled enrollments are final.";
            case ENROLL_INACTIVE_STUDENT:
                return "Inactive student cannot be enrolled.";
            case DEACTIVATE_STUDENT_HAS_OPEN_ENROLLMENTS:
                return "Student with open enrollments cannot be deactivated.";
            case DEACTIVATE_COURSE_HAS_ACTIVE_ENROLLMENTS:
                return "Course with active enrollments cannot be deactivated.";
            case DEACTIVATE_TRAINER_ASSIGNED_TO_ACTIVE_COURSE:
                return "Trainer assigned to active course cannot be deactivated.";
            case INPUT_POSITIVE_NUMBER_REQUIRED:
                return "A positive number is required.";
            case INPUT_MENU_CHOICE_INVALID:
                return "Menu choice is not available.";
            default:
                return "Unknown LearnTrack rule.";
        }
    }

    public String format() {
        return getCode() + ": " + getMessage();
    }
}
