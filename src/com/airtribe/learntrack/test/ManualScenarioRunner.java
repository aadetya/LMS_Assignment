package com.airtribe.learntrack.test;

import com.airtribe.learntrack.service.ActionLogService;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.GuidedDemoService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.ReportService;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.service.TrainerService;
import com.airtribe.learntrack.util.SeedDataLoader;

public class ManualScenarioRunner {
    public static void main(String[] args) {
        ActionLogService actionLogService = new ActionLogService();
        ReceiptService receiptService = new ReceiptService();
        StudentService studentService = new StudentService(actionLogService, receiptService);
        TrainerService trainerService = new TrainerService(actionLogService, receiptService);
        CourseService courseService = new CourseService(trainerService, actionLogService, receiptService);
        EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService, actionLogService, receiptService);
        ReportService reportService = new ReportService(studentService, trainerService, courseService, enrollmentService, actionLogService, receiptService);
        GuidedDemoService guidedDemoService = new GuidedDemoService(studentService, trainerService, courseService, enrollmentService, reportService, actionLogService, receiptService);

        SeedDataLoader.load(studentService, trainerService, courseService, enrollmentService, actionLogService, receiptService);
        guidedDemoService.runGuidedDemo();
        reportService.printLearningPulseDashboard();
        reportService.printCohortHealthScore();
        reportService.printCapacityReport();
        reportService.printPeopleDirectory();
        reportService.printActionJournal(12);
        reportService.printRecentReceipts(12);
    }
}
