package com.airtribe.learntrack.util;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.EnrollmentDecision;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.entity.enums.ActionType;
import com.airtribe.learntrack.entity.enums.CourseLevel;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.service.ActionLogService;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.service.TrainerService;

public final class SeedDataLoader {
    private SeedDataLoader() {
    }

    public static boolean load(StudentService studentService, TrainerService trainerService, CourseService courseService, EnrollmentService enrollmentService, ActionLogService actionLogService, ReceiptService receiptService) {
        try {
            Student ananya = studentService.addStudent("Ananya", "Sharma", "ananya@learntrack.dev", "Java Foundations", "Build backend confidence");
            Student kabir = studentService.addStudent("Kabir", "Rao", "kabir@learntrack.dev", "Java Foundations", "Master Core Java");
            Student meera = studentService.addStudent("Meera", "Iyer", "meera@learntrack.dev", "Weekend Core", "Prepare for interviews");
            Student dev = studentService.addStudent("Dev", "Kapoor", "dev@learntrack.dev", "Capstone Prep", "Complete a portfolio project");

            Trainer rohit = trainerService.addTrainer("Rohit", "Mehta", "rohit@learntrack.dev", "Core Java");
            Trainer nisha = trainerService.addTrainer("Nisha", "Sen", "nisha@learntrack.dev", "Course Design");

            Course javaLab = courseService.addCourse("Core Java Foundations", "Beginner-safe Java fundamentals with labs.", 8, 1, CourseLevel.FOUNDATION);
            Course collections = courseService.addCourse("ArrayList Operations Lab", "Practice collection-backed learning workflows.", 4, 2, CourseLevel.CORE);
            Course testing = courseService.addCourse("Console Testing Clinic", "Manual test cases and smoke checks.", 3, 4, CourseLevel.ELECTIVE);
            Course capstone = courseService.addCourse("Learning Ops Capstone", "Demo-ready operations scenario.", 6, 3, CourseLevel.CAPSTONE);

            courseService.assignTrainer(javaLab.getId(), rohit.getId());
            courseService.assignTrainer(collections.getId(), nisha.getId());
            courseService.openEnrollmentWindow(javaLab.getId());
            courseService.openEnrollmentWindow(collections.getId());
            courseService.openEnrollmentWindow(capstone.getId());

            EnrollmentDecision firstDecision = enrollmentService.enrollStudent(ananya.getId(), javaLab.getId());
            EnrollmentDecision waitlistDecision = enrollmentService.enrollStudent(kabir.getId(), javaLab.getId());
            EnrollmentDecision completedDecision = enrollmentService.enrollStudent(meera.getId(), collections.getId());
            EnrollmentDecision cancelledDecision = enrollmentService.enrollStudent(dev.getId(), collections.getId());

            enrollmentService.markEnrollmentCompleted(completedDecision.getEnrollment().getId(), "Seed data completed trail");
            enrollmentService.cancelEnrollment(cancelledDecision.getEnrollment().getId(), "Seed data cancellation sample");

            actionLogService.record(ActionType.SEED_DATA_LOADED, "Demo cohort data loaded with waitlist and lifecycle samples.", "System", "SYSTEM", 1);
            receiptService.issueReceipt("LOAD_SEED_DATA", "LOADED", "Seed data created 4 learners, 2 trainers, 4 courses, and sample enrollments.", RuleCode.ENROLL_CAPACITY_FULL_WAITLIST.format(), "Open Guided Demo or Reports & Signals.", "SYSTEM", 1);

            ConsolePrinter.printSuccess("Demo data loaded. Waitlist sample enrollment #" + waitlistDecision.getEnrollment().getId() + " is ready.");
            if (firstDecision.isAccepted()) {
                ConsolePrinter.printInfo("First seed learner was accepted into " + javaLab.getCourseCode() + ".");
            }
            if (testing.getId() > 0) {
                ConsolePrinter.printInfo("A closed elective course is present for catalog operations.");
            }
            return true;
        } catch (Exception exception) {
            ConsolePrinter.printError("Seed data could not be loaded: " + exception.getMessage());
            return false;
        }
    }
}
