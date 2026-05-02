package com.airtribe.learntrack.util;

import com.airtribe.learntrack.entity.enums.CourseLevel;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.InvalidInputException;

public final class InputValidator {
    private InputValidator() {
    }

    public static void requireNonBlank(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().length() == 0) {
            String lowerField = fieldName == null ? "" : fieldName.toLowerCase();
            if (lowerField.indexOf("course") >= 0) {
                throw new InvalidInputException(RuleCode.COURSE_NAME_REQUIRED.format() + " Field: " + fieldName);
            }
            throw new InvalidInputException(RuleCode.STUDENT_NAME_REQUIRED.format() + " Field: " + fieldName);
        }
    }

    public static void requirePositiveInt(int value, String fieldName) throws InvalidInputException {
        if (value <= 0) {
            String lowerField = fieldName == null ? "" : fieldName.toLowerCase();
            if (lowerField.indexOf("seat") >= 0 || lowerField.indexOf("capacity") >= 0) {
                throw new InvalidInputException(RuleCode.COURSE_CAPACITY_POSITIVE.format() + " Field: " + fieldName);
            }
            throw new InvalidInputException(RuleCode.INPUT_POSITIVE_NUMBER_REQUIRED.format() + " Field: " + fieldName);
        }
    }

    public static void requireNonNegativeInt(int value, String fieldName) throws InvalidInputException {
        if (value < 0) {
            throw new InvalidInputException(RuleCode.INPUT_POSITIVE_NUMBER_REQUIRED.format() + " Field: " + fieldName);
        }
    }

    public static int parsePositiveInt(String rawValue, String fieldName) throws InvalidInputException {
        try {
            int value = Integer.parseInt(rawValue == null ? "" : rawValue.trim());
            requirePositiveInt(value, fieldName);
            return value;
        } catch (NumberFormatException exception) {
            throw new InvalidInputException(RuleCode.INPUT_POSITIVE_NUMBER_REQUIRED.format() + " Field: " + fieldName, exception);
        }
    }

    public static void validateEmailIfPresent(String email) throws InvalidInputException {
        if (email == null || email.trim().length() == 0) {
            return;
        }
        String normalized = normalizeEmail(email);
        if (normalized.indexOf("@") < 1 || normalized.indexOf(".") < 0) {
            throw new InvalidInputException(RuleCode.STUDENT_EMAIL_INVALID.format() + " Value: " + email);
        }
    }

    public static String normalizeName(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    public static String normalizeEmail(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    public static String normalizeCode(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toUpperCase();
    }

    public static CourseLevel parseCourseLevel(String value) throws InvalidInputException {
        String normalized = normalizeCode(value);
        CourseLevel[] levels = CourseLevel.values();
        for (int i = 0; i < levels.length; i++) {
            if (levels[i].name().equals(normalized)) {
                return levels[i];
            }
        }
        throw new InvalidInputException(RuleCode.INPUT_MENU_CHOICE_INVALID.format() + " Course level must be FOUNDATION, CORE, ELECTIVE, or CAPSTONE.");
    }
}
