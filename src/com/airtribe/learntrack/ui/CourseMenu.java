package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.enums.CourseLevel;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.ReportService;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.TextTablePrinter;
import java.util.ArrayList;

public class CourseMenu {
    private ConsoleReader reader;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private ReportService reportService;
    private ReceiptService receiptService;

    public CourseMenu(ConsoleReader reader, CourseService courseService, EnrollmentService enrollmentService, ReportService reportService, ReceiptService receiptService) {
        this.reader = reader;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.reportService = reportService;
        this.receiptService = receiptService;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            try {
                printMenu();
                int choice = reader.readMenuChoice("Choose option: ");
                switch (choice) {
                    case 1:
                        addCourse();
                        break;
                    case 2:
                        TextTablePrinter.printCourses(courseService.getAllCourses());
                        break;
                    case 3:
                        TextTablePrinter.printCourses(courseService.getActiveCourses());
                        break;
                    case 4:
                        TextTablePrinter.printCourses(courseService.getOpenCourses());
                        break;
                    case 5:
                        System.out.println(courseService.findCourseById(reader.readPositiveInt("Course ID: ")).toString());
                        break;
                    case 6:
                        System.out.println(courseService.findCourseByCode(reader.readLine("Course code: ")).toString());
                        break;
                    case 7:
                        TextTablePrinter.printCourses(courseService.searchCoursesByName(reader.readLine("Course keyword: ")));
                        break;
                    case 8:
                        updateCourse();
                        break;
                    case 9:
                        printReceipt(courseService.openEnrollmentWindow(reader.readPositiveInt("Course ID: ")));
                        break;
                    case 10:
                        printReceipt(courseService.closeEnrollmentWindow(reader.readPositiveInt("Course ID: ")));
                        break;
                    case 11:
                        printReceipt(courseService.assignTrainer(reader.readPositiveInt("Course ID: "), reader.readPositiveInt("Trainer ID: ")));
                        break;
                    case 12:
                        printReceipt(courseService.unassignTrainer(reader.readPositiveInt("Course ID: ")));
                        break;
                    case 13:
                        printReceipt(courseService.deactivateCourse(reader.readPositiveInt("Course ID: "), enrollmentService.getAllEnrollments()));
                        break;
                    case 14:
                        printReceipt(courseService.reactivateCourse(reader.readPositiveInt("Course ID: ")));
                        break;
                    case 15:
                        reportService.printCourseOperationsCard(reader.readPositiveInt("Course ID: "));
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
            }
        }
    }

    private void printMenu() {
        ConsolePrinter.printHeader("Course Catalog Ops");
        ConsolePrinter.printMenuOption("1", "Add course");
        ConsolePrinter.printMenuOption("2", "View all courses");
        ConsolePrinter.printMenuOption("3", "View active courses");
        ConsolePrinter.printMenuOption("4", "View open courses");
        ConsolePrinter.printMenuOption("5", "Search course by ID");
        ConsolePrinter.printMenuOption("6", "Search course by code");
        ConsolePrinter.printMenuOption("7", "Search courses by name");
        ConsolePrinter.printMenuOption("8", "Update course");
        ConsolePrinter.printMenuOption("9", "Open enrollment window");
        ConsolePrinter.printMenuOption("10", "Close enrollment window");
        ConsolePrinter.printMenuOption("11", "Assign trainer");
        ConsolePrinter.printMenuOption("12", "Unassign trainer");
        ConsolePrinter.printMenuOption("13", "Safe deactivate course");
        ConsolePrinter.printMenuOption("14", "Reactivate course");
        ConsolePrinter.printMenuOption("15", "View Course Operations Card");
        ConsolePrinter.printMenuOption("0", "Back");
    }

    private void addCourse() throws InvalidInputException, DuplicateEntityException {
        String name = reader.readLine("Course name: ");
        String description = reader.readOptionalLine("Description: ");
        int duration = reader.readPositiveInt("Duration in weeks: ");
        int seats = reader.readPositiveInt("Seat capacity: ");
        CourseLevel level = reader.readCourseLevel("Level (FOUNDATION/CORE/ELECTIVE/CAPSTONE): ");
        Course course = courseService.addCourse(name, description, duration, seats, level);
        ConsolePrinter.printSuccess(course.getShortSummary());
        printLatestReceipt("COURSE", course.getId());
    }

    private void updateCourse() throws InvalidInputException, DuplicateEntityException, EntityNotFoundException {
        int id = reader.readPositiveInt("Course ID: ");
        String name = reader.readLine("Course name: ");
        String description = reader.readOptionalLine("Description: ");
        int duration = reader.readPositiveInt("Duration in weeks: ");
        int seats = reader.readPositiveInt("Seat capacity: ");
        CourseLevel level = reader.readCourseLevel("Level (FOUNDATION/CORE/ELECTIVE/CAPSTONE): ");
        Course course = courseService.updateCourse(id, name, description, duration, seats, level);
        ConsolePrinter.printSuccess(course.getShortSummary());
        printLatestReceipt("COURSE", course.getId());
    }

    private void printReceipt(OperationReceipt receipt) {
        System.out.println(receipt.toDisplayBlock());
    }

    private void printLatestReceipt(String referenceType, int referenceId) {
        ArrayList<OperationReceipt> receipts = receiptService.findReceiptsByReference(referenceType, referenceId);
        if (receipts.size() > 0) {
            printReceipt(receipts.get(receipts.size() - 1));
        }
    }
}
