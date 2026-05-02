package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.ActionLogEntry;
import com.airtribe.learntrack.entity.enums.ActionType;
import com.airtribe.learntrack.util.IdGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ActionLogService {
    private ArrayList<ActionLogEntry> entries = new ArrayList<ActionLogEntry>();

    public ActionLogEntry record(ActionType type, String summary, String actor, String referenceType, int referenceId) {
        ActionLogEntry entry = new ActionLogEntry(
                IdGenerator.getNextActionLogId(),
                LocalDateTime.now(),
                type,
                summary,
                actor,
                referenceType,
                referenceId);
        entries.add(entry);
        return entry;
    }

    public ArrayList<ActionLogEntry> getAllEntries() {
        return new ArrayList<ActionLogEntry>(entries);
    }

    public ArrayList<ActionLogEntry> getRecentEntries(int limit) {
        ArrayList<ActionLogEntry> recent = new ArrayList<ActionLogEntry>();
        if (limit <= 0) {
            return recent;
        }
        int added = 0;
        for (int i = entries.size() - 1; i >= 0 && added < limit; i--) {
            recent.add(entries.get(i));
            added++;
        }
        return recent;
    }

    public ArrayList<ActionLogEntry> findByReference(String referenceType, int referenceId) {
        ArrayList<ActionLogEntry> matches = new ArrayList<ActionLogEntry>();
        for (int i = 0; i < entries.size(); i++) {
            ActionLogEntry entry = entries.get(i);
            if (entry.getReferenceId() == referenceId && entry.getReferenceType() != null && entry.getReferenceType().equalsIgnoreCase(referenceType)) {
                matches.add(entry);
            }
        }
        return matches;
    }

    public int count() {
        return entries.size();
    }
}
