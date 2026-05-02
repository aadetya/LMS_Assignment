package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.EnrollmentDecision;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.enums.ActionType;
import com.airtribe.learntrack.entity.enums.CourseLevel;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.TextTablePrinter;

public class GuidedDemoService {
    private StudentService studentService;
    private TrainerService trainerService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private ReportService reportService;
    private ActionLogService actionLogService;
    private ReceiptService receiptService;

    public GuidedDemoService(StudentService studentService, TrainerService trainerService, CourseService courseService, EnrollmentService enrollmentService, ReportService reportService, ActionLogService actionLogService, ReceiptService receiptService) {
        this.studentService = studentService;
        this.trainerService = trainerService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.reportService = reportService;
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
    }

    public void runGuidedDemo() {
        ConsolePrinter.printHeader("Guided Demo");
        ConsolePrinter.printInfo("This walkthrough demonstrates capacity, waitlist, promotion, receipts, and reports.");
        try {
            int suffix = receiptService.count() + actionLogService.count() + 1;
            Course course = courseService.addCourse("Demo Capacity Lab " + suffix, "Capacity-one course for the guided walkthrough.", 2, 1, CourseLevel.CORE);
            courseService.openEnrollmentWindow(course.getId());

            Student first = studentService.addStudent("Riya", "Demo" + suffix, "riya.demo" + suffix + "@learntrack.dev", "Guided Demo", "Watch waitlist promotion");
            Student second = studentService.addStudent("Aman", "Demo" + suffix, "aman.demo" + suffix + "@learntrack.dev", "Guided Demo", "Observe EnrollmentDecision");

            ConsolePrinter.printSubHeader("Step 1: Enrollment Decisions");
            EnrollmentDecision firstDecision = enrollmentService.enrollStudent(first.getId(), course.getId());
            System.out.println(firstDecision.toDisplayBlock());
            System.out.println(firstDecision.getReceipt().toDisplayBlock());

            EnrollmentDecision secondDecision = enrollmentService.enrollStudent(second.getId(), course.getId());
            System.out.println(secondDecision.toDisplayBlock());
            System.out.println(secondDecision.getReceipt().toDisplayBlock());

            ConsolePrinter.printSubHeader("Step 2: Capacity Before Cancellation");
            reportService.printCourseOperationsCard(course.getId());

            ConsolePrinter.printSubHeader("Step 3: Cancel Active Enrollment");
            OperationReceipt cancellationReceipt = enrollmentService.cancelEnrollment(firstDecision.getEnrollment().getId(), "Guided demo cancellation to trigger Waitlist Promotion");
            System.out.println(cancellationReceipt.toDisplayBlock());
            ConsolePrinter.printSuccess("Automatic Waitlist Promotion checked after cancellation.");

            ConsolePrinter.printSubHeader("Step 4: Recent Operation Receipts");
            TextTablePrinter.printReceipts(receiptService.getRecentReceipts(6));
            ConsolePrinter.printSubHeader("Step 5: Recent Action Journal");
            TextTablePrinter.printActionLog(actionLogService.getRecentEntries(6));

            ConsolePrinter.printSubHeader("Step 6: Capacity After Promotion");
            reportService.printCourseOperationsCard(course.getId());
            ConsolePrinter.printSubHeader("Step 7: Cohort Health Score");
            reportService.printCohortHealthScore();

            actionLogService.record(ActionType.GUIDED_DEMO_RAN, "Guided Demo completed.", "System", "SYSTEM", course.getId());
            receiptService.issueReceipt("GUIDED_DEMO", "COMPLETED", "Guided Demo completed for " + course.getCourseCode() + ".", RuleCode.ENROLL_CAPACITY_FULL_WAITLIST.format(), "Check receipts and Cohort Health Score.", "SYSTEM", course.getId());
        } catch (Exception exception) {
            ConsolePrinter.printError("Guided Demo stopped: " + exception.getMessage());
        }
    }
}
