package com.airtribe.learntrack.entity.enums;

public enum CourseLevel {
    FOUNDATION,
    CORE,
    ELECTIVE,
    CAPSTONE;

    public String getDisplayText() {
        switch (this) {
            case FOUNDATION:
                return "Foundation";
            case CORE:
                return "Core";
            case ELECTIVE:
                return "Elective";
            case CAPSTONE:
                return "Capstone";
            default:
                return "Unknown";
        }
    }
}
