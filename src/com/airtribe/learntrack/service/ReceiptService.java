package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.util.IdGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReceiptService {
    private ArrayList<OperationReceipt> receipts = new ArrayList<OperationReceipt>();

    public OperationReceipt issueReceipt(String operationName, String outcome, String primaryMessage, String ruleSummary, String nextSuggestedAction, String referenceType, int referenceId) {
        OperationReceipt receipt = new OperationReceipt(
                IdGenerator.getNextReceiptId(),
                LocalDateTime.now(),
                operationName,
                outcome,
                primaryMessage,
                ruleSummary,
                nextSuggestedAction,
                referenceType,
                referenceId);
        receipts.add(receipt);
        return receipt;
    }

    public ArrayList<OperationReceipt> getAllReceipts() {
        return new ArrayList<OperationReceipt>(receipts);
    }

    public ArrayList<OperationReceipt> getRecentReceipts(int limit) {
        ArrayList<OperationReceipt> recent = new ArrayList<OperationReceipt>();
        if (limit <= 0) {
            return recent;
        }
        int added = 0;
        for (int i = receipts.size() - 1; i >= 0 && added < limit; i--) {
            recent.add(receipts.get(i));
            added++;
        }
        return recent;
    }

    public ArrayList<OperationReceipt> findReceiptsByReference(String referenceType, int referenceId) {
        ArrayList<OperationReceipt> matches = new ArrayList<OperationReceipt>();
        for (int i = 0; i < receipts.size(); i++) {
            OperationReceipt receipt = receipts.get(i);
            if (receipt.getReferenceId() == referenceId && receipt.getReferenceType() != null && receipt.getReferenceType().equalsIgnoreCase(referenceType)) {
                matches.add(receipt);
            }
        }
        return matches;
    }

    public int count() {
        return receipts.size();
    }
}
