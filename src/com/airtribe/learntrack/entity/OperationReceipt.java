package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.util.ConsolePrinter;
import java.time.LocalDateTime;

public class OperationReceipt {
    private int receiptId;
    private LocalDateTime createdAt;
    private String operationName;
    private String outcome;
    private String primaryMessage;
    private String ruleSummary;
    private String nextSuggestedAction;
    private String referenceType;
    private int referenceId;

    public OperationReceipt() {
    }

    public OperationReceipt(int receiptId, LocalDateTime createdAt, String operationName, String outcome, String primaryMessage, String ruleSummary, String nextSuggestedAction, String referenceType, int referenceId) {
        this.receiptId = receiptId;
        this.createdAt = createdAt;
        this.operationName = operationName;
        this.outcome = outcome;
        this.primaryMessage = primaryMessage;
        this.ruleSummary = ruleSummary;
        this.nextSuggestedAction = nextSuggestedAction;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getPrimaryMessage() {
        return primaryMessage;
    }

    public void setPrimaryMessage(String primaryMessage) {
        this.primaryMessage = primaryMessage;
    }

    public String getRuleSummary() {
        return ruleSummary;
    }

    public void setRuleSummary(String ruleSummary) {
        this.ruleSummary = ruleSummary;
    }

    public String getNextSuggestedAction() {
        return nextSuggestedAction;
    }

    public void setNextSuggestedAction(String nextSuggestedAction) {
        this.nextSuggestedAction = nextSuggestedAction;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String toDisplayBlock() {
        return ConsolePrinter.formatKeyValueBlock("Receipt #" + receiptId, new String[][] {
                {"Operation", operationName},
                {"Outcome", outcome},
                {"Message", primaryMessage},
                {"Rule", ruleSummary},
                {"Next Step", nextSuggestedAction},
                {"Reference", referenceType + " #" + referenceId}
        });
    }

    public String toString() {
        return "Receipt #" + receiptId + " | " + operationName + " | " + outcome + " | " + referenceType + " #" + referenceId;
    }
}
