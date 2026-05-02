package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.ReportService;
import com.airtribe.learntrack.util.ConsolePrinter;

public class ReportMenu {
    private ConsoleReader reader;
    private ReportService reportService;

    public ReportMenu(ConsoleReader reader, ReportService reportService) {
        this.reader = reader;
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
                        reportService.printLearningPulseDashboard();
                        break;
                    case 2:
                        reportService.printCohortHealthScore();
                        break;
                    case 3:
                        reportService.printCourseWiseEnrollmentCounts();
                        break;
                    case 4:
                        reportService.printCapacityReport();
                        break;
                    case 5:
                        reportService.printWaitlistReport();
                        break;
                    case 6:
                        reportService.printTrainerCoverageReport();
                        break;
                    case 7:
                        reportService.printPeopleDirectory();
                        break;
                    case 8:
                        reportService.printCourseOperationsCard(reader.readPositiveInt("Course ID: "));
                        break;
                    case 9:
                        reportService.printStudentLearningTrail(reader.readPositiveInt("Student ID: "));
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
        ConsolePrinter.printHeader("Reports & Signals");
        ConsolePrinter.printMenuOption("1", "Learning Pulse Dashboard");
        ConsolePrinter.printMenuOption("2", "Cohort Health Score");
        ConsolePrinter.printMenuOption("3", "Course-wise Enrollment Counts");
        ConsolePrinter.printMenuOption("4", "Capacity Report");
        ConsolePrinter.printMenuOption("5", "Waitlist Report");
        ConsolePrinter.printMenuOption("6", "Trainer Coverage Report");
        ConsolePrinter.printMenuOption("7", "People Directory");
        ConsolePrinter.printMenuOption("8", "Course Operations Card");
        ConsolePrinter.printMenuOption("9", "Student Learning Trail");
        ConsolePrinter.printMenuOption("0", "Back");
    }
}
