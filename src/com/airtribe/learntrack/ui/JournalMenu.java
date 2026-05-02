package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.ActionLogService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.ReportService;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.TextTablePrinter;

public class JournalMenu {
    private ConsoleReader reader;
    private ActionLogService actionLogService;
    private ReceiptService receiptService;
    private ReportService reportService;

    public JournalMenu(ConsoleReader reader, ActionLogService actionLogService, ReceiptService receiptService, ReportService reportService) {
        this.reader = reader;
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
        this.reportService = reportService;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            try {
                printMenu();
                int choice = reader.readMenuChoice("Choose option: ");
                switch (choice) {
                    case 1:
                        reportService.printActionJournal(reader.readPositiveInt("Recent limit: "));
                        break;
                    case 2:
                        TextTablePrinter.printActionLog(actionLogService.getAllEntries());
                        break;
                    case 3:
                        reportService.printRecentReceipts(reader.readPositiveInt("Recent limit: "));
                        break;
                    case 4:
                        TextTablePrinter.printReceipts(receiptService.getAllReceipts());
                        break;
                    case 5:
                        reportService.printReceiptsForReference(reader.readLine("Reference type (STUDENT/COURSE/ENROLLMENT/TRAINER/SYSTEM): "), reader.readPositiveInt("Reference ID: "));
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        throw new InvalidInputException(RuleCode.INPUT_MENU_CHOICE_INVALID.format() + " Choice: " + choice);
                }
                if (!back) {
                    reader.pause();
                }
            } catch (InvalidInputException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            }
        }
    }

    private void printMenu() {
        ConsolePrinter.printHeader("Action Journal & Receipts");
        ConsolePrinter.printMenuOption("1", "View recent action journal");
        ConsolePrinter.printMenuOption("2", "View all action journal entries");
        ConsolePrinter.printMenuOption("3", "View recent operation receipts");
        ConsolePrinter.printMenuOption("4", "View all operation receipts");
        ConsolePrinter.printMenuOption("5", "Find receipts by reference");
        ConsolePrinter.printMenuOption("0", "Back");
    }
}
