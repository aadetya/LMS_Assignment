package com.airtribe.learntrack.util;

public final class CourseCodeGenerator {
    private CourseCodeGenerator() {
    }

    public static String generateCourseCode(String courseName, int courseId) {
        String name = courseName == null ? "" : courseName.trim();
        StringBuilder initials = new StringBuilder();
        boolean nextLetterStartsWord = true;
        for (int i = 0; i < name.length(); i++) {
            char current = name.charAt(i);
            if (current == ' ' || current == '-' || current == '_') {
                nextLetterStartsWord = true;
            } else if (nextLetterStartsWord) {
                initials.append(Character.toUpperCase(current));
                nextLetterStartsWord = false;
            }
        }
        if (initials.length() == 0) {
            initials.append("GEN");
        }
        return "LT-" + initials.toString() + "-" + courseId;
    }
}
