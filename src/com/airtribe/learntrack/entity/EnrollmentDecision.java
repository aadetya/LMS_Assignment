package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.util.ConsolePrinter;

public class EnrollmentDecision {
    private boolean accepted;
    private boolean waitlisted;
    private boolean rejected;
    private Enrollment enrollment;
    private OperationReceipt receipt;
    private String decisionReason;
    private String ruleCodeApplied;

    public EnrollmentDecision() {
    }

    public EnrollmentDecision(boolean accepted, boolean waitlisted, boolean rejected, Enrollment enrollment, OperationReceipt receipt, String decisionReason, String ruleCodeApplied) {
        this.accepted = accepted;
        this.waitlisted = waitlisted;
        this.rejected = rejected;
        this.enrollment = enrollment;
        this.receipt = receipt;
        this.decisionReason = decisionReason;
        this.ruleCodeApplied = ruleCodeApplied;
    }

    public static EnrollmentDecision accepted(Enrollment enrollment, OperationReceipt receipt, String reason, String ruleCode) {
        return new EnrollmentDecision(true, false, false, enrollment, receipt, reason, ruleCode);
    }

    public static EnrollmentDecision waitlisted(Enrollment enrollment, OperationReceipt receipt, String reason, String ruleCode) {
        return new EnrollmentDecision(false, true, false, enrollment, receipt, reason, ruleCode);
    }

    public static EnrollmentDecision rejected(OperationReceipt receipt, String reason, String ruleCode) {
        return new EnrollmentDecision(false, false, true, null, receipt, reason, ruleCode);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isWaitlisted() {
        return waitlisted;
    }

    public void setWaitlisted(boolean waitlisted) {
        this.waitlisted = waitlisted;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public OperationReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(OperationReceipt receipt) {
        this.receipt = receipt;
    }

    public String getDecisionReason() {
        return decisionReason;
    }

    public void setDecisionReason(String decisionReason) {
        this.decisionReason = decisionReason;
    }

    public String getRuleCodeApplied() {
        return ruleCodeApplied;
    }

    public void setRuleCodeApplied(String ruleCodeApplied) {
        this.ruleCodeApplied = ruleCodeApplied;
    }

    public String getDecisionLabel() {
        if (accepted) {
            return "ACCEPTED";
        }
        if (waitlisted) {
            return "WAITLISTED";
        }
        if (rejected) {
            return "REJECTED";
        }
        return "UNKNOWN";
    }

    public String toDisplayBlock() {
        String enrollmentSummary = "No enrollment created";
        if (enrollment != null) {
            enrollmentSummary = "#" + enrollment.getId() + " | Status: " + enrollment.getStatus().getDisplayText();
        }
        return ConsolePrinter.formatKeyValueBlock("Enrollment Decision", new String[][] {
                {"Decision", getDecisionLabel()},
                {"Reason", decisionReason},
                {"Rule Applied", ruleCodeApplied},
                {"Enrollment", enrollmentSummary}
        });
    }

    public String toString() {
        return getDecisionLabel() + " | " + decisionReason;
    }
}
