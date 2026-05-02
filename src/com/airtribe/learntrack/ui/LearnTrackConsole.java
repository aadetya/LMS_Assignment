package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.ActionLogService;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.GuidedDemoService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.ReportService;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.service.TrainerService;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.SeedDataLoader;
import java.util.Scanner;

public class LearnTrackConsole {
    private ConsoleReader reader;
    private StudentMenu studentMenu;
    private TrainerMenu trainerMenu;
    private CourseMenu courseMenu;
    private EnrollmentMenu enrollmentMenu;
    private ReportMenu reportMenu;
    private JournalMenu journalMenu;
    private GuidedDemoService guidedDemoService;
    private boolean seedDataLoaded;
    private StudentService studentService;
    private TrainerService trainerService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private ActionLogService actionLogService;
    private ReceiptService receiptService;

    public LearnTrackConsole(StudentService studentService, TrainerService trainerService, CourseService courseService, EnrollmentService enrollmentService, ReportService reportService, GuidedDemoService guidedDemoService, ActionLogService actionLogService, ReceiptService receiptService) {
        this.reader = new ConsoleReader(new Scanner(System.in));
        this.studentService = studentService;
        this.trainerService = trainerService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
        this.guidedDemoService = guidedDemoService;
        this.studentMenu = new StudentMenu(reader, studentService, enrollmentService, reportService, receiptService);
        this.trainerMenu = new TrainerMenu(reader, trainerService, courseService, receiptService);
        this.courseMenu = new CourseMenu(reader, courseService, enrollmentService, reportService, receiptService);
        this.enrollmentMenu = new EnrollmentMenu(reader, enrollmentService);
        this.reportMenu = new ReportMenu(reader, reportService);
        this.journalMenu = new JournalMenu(reader, actionLogService, receiptService, reportService);
        this.seedDataLoaded = false;
    }

    public void start() {
        ConsolePrinter.printAppBanner();
        boolean running = true;
        while (running) {
            try {
                showMainMenu();
                int choice = reader.readMenuChoice("Choose option: ");
                switch (choice) {
                    case 1:
                        studentMenu.start();
                        break;
                    case 2:
                        trainerMenu.start();
                        break;
                    case 3:
                        courseMenu.start();
                        break;
                    case 4:
                        enrollmentMenu.start();
                        break;
                    case 5:
                        reportMenu.start();
                        break;
                    case 6:
                        journalMenu.start();
                        break;
                    case 7:
                        handleLoadSeedData();
                        reader.pause();
                        break;
                    case 8:
                        guidedDemoService.runGuidedDemo();
                        reader.pause();
                        break;
                    case 0:
                        running = false;
                        ConsolePrinter.printSuccess("LearnTrack session closed.");
                        break;
                    default:
                        throw new InvalidInputException(RuleCode.INPUT_MENU_CHOICE_INVALID.format() + " Choice: " + choice);
                }
            } catch (InvalidInputException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            }
        }
    }

    public void showMainMenu() {
        ConsolePrinter.printHeader("Main Menu");
        ConsolePrinter.printMenuOption("1", "Learner Desk");
        ConsolePrinter.printMenuOption("2", "Trainer Desk");
        ConsolePrinter.printMenuOption("3", "Course Catalog Ops");
        ConsolePrinter.printMenuOption("4", "Enrollment Desk");
        ConsolePrinter.printMenuOption("5", "Reports & Signals");
        ConsolePrinter.printMenuOption("6", "Action Journal & Receipts");
        ConsolePrinter.printMenuOption("7", "Load Demo Data");
        ConsolePrinter.printMenuOption("8", "Guided Demo");
        ConsolePrinter.printMenuOption("0", "Exit");
    }

    public void handleLoadSeedData() {
        if (seedDataLoaded) {
            ConsolePrinter.printWarning("Demo data is already loaded for this session.");
            return;
        }
        seedDataLoaded = SeedDataLoader.load(studentService, trainerService, courseService, enrollmentService, actionLogService, receiptService);
    }
}
