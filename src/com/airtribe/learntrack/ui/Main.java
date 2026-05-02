package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.service.ActionLogService;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.GuidedDemoService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.ReportService;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.service.TrainerService;

public class Main {
    public static void main(String[] args) {
        ActionLogService actionLogService = new ActionLogService();
        ReceiptService receiptService = new ReceiptService();
        StudentService studentService = new StudentService(actionLogService, receiptService);
        TrainerService trainerService = new TrainerService(actionLogService, receiptService);
        CourseService courseService = new CourseService(trainerService, actionLogService, receiptService);
        EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService, actionLogService, receiptService);
        ReportService reportService = new ReportService(studentService, trainerService, courseService, enrollmentService, actionLogService, receiptService);
        GuidedDemoService guidedDemoService = new GuidedDemoService(studentService, trainerService, courseService, enrollmentService, reportService, actionLogService, receiptService);
        LearnTrackConsole console = new LearnTrackConsole(studentService, trainerService, courseService, enrollmentService, reportService, guidedDemoService, actionLogService, receiptService);
        console.start();
    }
}
