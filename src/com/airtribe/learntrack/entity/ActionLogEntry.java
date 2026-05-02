package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.entity.enums.ActionType;
import java.time.LocalDateTime;

public class ActionLogEntry {
    private int id;
    private LocalDateTime occurredAt;
    private ActionType actionType;
    private String summary;
    private String actor;
    private String referenceType;
    private int referenceId;

    public ActionLogEntry() {
    }

    public ActionLogEntry(int id, LocalDateTime occurredAt, ActionType actionType, String summary, String actor, String referenceType, int referenceId) {
        this.id = id;
        this.occurredAt = occurredAt;
        this.actionType = actionType;
        this.summary = summary;
        this.actor = actor;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
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

    public String toString() {
        String action = actionType == null ? "Unknown" : actionType.getDisplayText();
        return "#" + id + " | " + occurredAt + " | " + action + " | " + summary + " | " + actor + " | " + referenceType + " #" + referenceId;
    }
}
