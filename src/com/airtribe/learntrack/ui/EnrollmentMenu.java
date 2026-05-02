package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.EnrollmentDecision;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidEnrollmentStateException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.TextTablePrinter;

public class EnrollmentMenu {
    private ConsoleReader reader;
    private EnrollmentService enrollmentService;

    public EnrollmentMenu(ConsoleReader reader, EnrollmentService enrollmentService) {
        this.reader = reader;
        this.enrollmentService = enrollmentService;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            try {
                printMenu();
                int choice = reader.readMenuChoice("Choose option: ");
                switch (choice) {
                    case 1:
                        runEnrollmentDecision();
                        break;
                    case 2:
                        TextTablePrinter.printEnrollments(enrollmentService.getAllEnrollments());
                        break;
                    case 3:
                        TextTablePrinter.printEnrollments(enrollmentService.getOpenEnrollments());
                        break;
                    case 4:
                        TextTablePrinter.printEnrollments(enrollmentService.getEnrollmentsForStudent(reader.readPositiveInt("Student ID: ")));
                        break;
                    case 5:
                        TextTablePrinter.printEnrollments(enrollmentService.getEnrollmentsForCourse(reader.readPositiveInt("Course ID: ")));
                        break;
                    case 6:
                        OperationReceipt completeReceipt = enrollmentService.markEnrollmentCompleted(reader.readPositiveInt("Enrollment ID: "), reader.readOptionalLine("Completion note: "));
                        System.out.println(completeReceipt.toDisplayBlock());
                        break;
                    case 7:
                        OperationReceipt cancelReceipt = enrollmentService.cancelEnrollment(reader.readPositiveInt("Enrollment ID: "), reader.readOptionalLine("Cancellation note: "));
                        System.out.println(cancelReceipt.toDisplayBlock());
                        ConsolePrinter.printInfo("If an active seat opened, Waitlist Promotion receipt is in recent receipts.");
                        break;
                    case 8:
                        TextTablePrinter.printEnrollments(enrollmentService.getWaitlistedEnrollmentsForCourse(reader.readPositiveInt("Course ID: ")));
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
            } catch (EntityNotFoundException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            } catch (DuplicateEntityException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            } catch (BusinessRuleViolationException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            } catch (InvalidEnrollmentStateException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            }
        }
    }

    private void printMenu() {
        ConsolePrinter.printHeader("Enrollment Desk");
        ConsolePrinter.printMenuOption("1", "Run enrollment decision");
        ConsolePrinter.printMenuOption("2", "View all enrollments");
        ConsolePrinter.printMenuOption("3", "View open enrollments");
        ConsolePrinter.printMenuOption("4", "View enrollments for learner");
        ConsolePrinter.printMenuOption("5", "View enrollments for course");
        ConsolePrinter.printMenuOption("6", "Mark enrollment completed");
        ConsolePrinter.printMenuOption("7", "Cancel enrollment");
        ConsolePrinter.printMenuOption("8", "View waitlist for course");
        ConsolePrinter.printMenuOption("0", "Back");
    }

    private void runEnrollmentDecision() throws InvalidInputException, EntityNotFoundException, DuplicateEntityException, BusinessRuleViolationException {
        int studentId = reader.readPositiveInt("Student ID: ");
        int courseId = reader.readPositiveInt("Course ID: ");
        EnrollmentDecision decision = enrollmentService.enrollStudent(studentId, courseId);
        System.out.println(decision.toDisplayBlock());
        if (decision.getReceipt() != null) {
            System.out.println(decision.getReceipt().toDisplayBlock());
        }
    }
}
