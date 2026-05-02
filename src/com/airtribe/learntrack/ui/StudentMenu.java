package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.ReportService;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.TextTablePrinter;
import java.util.ArrayList;

public class StudentMenu {
    private ConsoleReader reader;
    private StudentService studentService;
    private EnrollmentService enrollmentService;
    private ReportService reportService;
    private ReceiptService receiptService;

    public StudentMenu(ConsoleReader reader, StudentService studentService, EnrollmentService enrollmentService, ReportService reportService, ReceiptService receiptService) {
        this.reader = reader;
        this.studentService = studentService;
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
                        addLearner();
                        break;
                    case 2:
                        TextTablePrinter.printStudents(studentService.getAllStudents());
                        break;
                    case 3:
                        TextTablePrinter.printStudents(studentService.getActiveStudents());
                        break;
                    case 4:
                        System.out.println(studentService.findStudentById(reader.readPositiveInt("Student ID: ")).getStudentCard());
                        break;
                    case 5:
                        System.out.println(studentService.findStudentByEmail(reader.readLine("Email: ")).getStudentCard());
                        break;
                    case 6:
                        TextTablePrinter.printStudents(studentService.searchStudentsByName(reader.readLine("Name keyword: ")));
                        break;
                    case 7:
                        updateLearner();
                        break;
                    case 8:
                        OperationReceipt deactivateReceipt = studentService.deactivateStudent(reader.readPositiveInt("Student ID: "), enrollmentService.getAllEnrollments());
                        System.out.println(deactivateReceipt.toDisplayBlock());
                        break;
                    case 9:
                        OperationReceipt reactivateReceipt = studentService.reactivateStudent(reader.readPositiveInt("Student ID: "));
                        System.out.println(reactivateReceipt.toDisplayBlock());
                        break;
                    case 10:
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
        ConsolePrinter.printHeader("Learner Desk");
        ConsolePrinter.printMenuOption("1", "Add learner");
        ConsolePrinter.printMenuOption("2", "View all learners");
        ConsolePrinter.printMenuOption("3", "View active learners");
        ConsolePrinter.printMenuOption("4", "Search learner by ID");
        ConsolePrinter.printMenuOption("5", "Search learner by email");
        ConsolePrinter.printMenuOption("6", "Search learners by name");
        ConsolePrinter.printMenuOption("7", "Update learner profile");
        ConsolePrinter.printMenuOption("8", "Safe deactivate learner");
        ConsolePrinter.printMenuOption("9", "Reactivate learner");
        ConsolePrinter.printMenuOption("10", "View Student Learning Trail");
        ConsolePrinter.printMenuOption("0", "Back");
    }

    private void addLearner() throws InvalidInputException, DuplicateEntityException {
        String firstName = reader.readLine("First name: ");
        String lastName = reader.readLine("Last name: ");
        String email = reader.readOptionalLine("Email (optional): ");
        String batch = reader.readLine("Batch: ");
        String learningGoal = reader.readOptionalLine("Learning goal: ");
        Student student = studentService.addStudent(firstName, lastName, email, batch, learningGoal);
        ConsolePrinter.printSuccess(student.getStudentCard());
        printLatestReceipt("STUDENT", student.getId());
    }

    private void updateLearner() throws InvalidInputException, DuplicateEntityException, EntityNotFoundException {
        int id = reader.readPositiveInt("Student ID: ");
        String firstName = reader.readLine("First name: ");
        String lastName = reader.readLine("Last name: ");
        String email = reader.readOptionalLine("Email (optional): ");
        String batch = reader.readLine("Batch: ");
        String learningGoal = reader.readOptionalLine("Learning goal: ");
        Student student = studentService.updateStudent(id, firstName, lastName, email, batch, learningGoal);
        ConsolePrinter.printSuccess(student.getStudentCard());
        printLatestReceipt("STUDENT", student.getId());
    }

    private void printLatestReceipt(String referenceType, int referenceId) {
        ArrayList<OperationReceipt> receipts = receiptService.findReceiptsByReference(referenceType, referenceId);
        if (receipts.size() > 0) {
            System.out.println(receipts.get(receipts.size() - 1).toDisplayBlock());
        }
    }
}
