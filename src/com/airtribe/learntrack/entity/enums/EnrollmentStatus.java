package com.airtribe.learntrack.entity.enums;

public enum EnrollmentStatus {
    ACTIVE,
    WAITLISTED,
    COMPLETED,
    CANCELLED;

    public String getDisplayText() {
        switch (this) {
            case ACTIVE:
                return "Active";
            case WAITLISTED:
                return "Waitlisted";
            case COMPLETED:
                return "Completed";
            case CANCELLED:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }

    public boolean isOpen() {
        switch (this) {
            case ACTIVE:
            case WAITLISTED:
                return true;
            default:
                return false;
        }
    }

    public boolean isFinal() {
        switch (this) {
            case COMPLETED:
            case CANCELLED:
                return true;
            default:
                return false;
        }
    }

    public boolean occupiesSeat() {
        switch (this) {
            case ACTIVE:
                return true;
            default:
                return false;
        }
    }
}
