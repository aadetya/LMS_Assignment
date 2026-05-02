package com.airtribe.learntrack.test;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.EnrollmentDecision;
import com.airtribe.learntrack.entity.Person;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.entity.enums.CourseLevel;
import com.airtribe.learntrack.entity.enums.EnrollmentStatus;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.InvalidEnrollmentStateException;
import com.airtribe.learntrack.service.ActionLogService;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.service.TrainerService;
import java.util.ArrayList;

public class LearnTrackSmokeTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        ActionLogService actionLogService = new ActionLogService();
        ReceiptService receiptService = new ReceiptService();
        StudentService studentService = new StudentService(actionLogService, receiptService);
        TrainerService trainerService = new TrainerService(actionLogService, receiptService);
        CourseService courseService = new CourseService(trainerService, actionLogService, receiptService);
        EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService, actionLogService, receiptService);

        try {
            Student firstStudent = studentService.addStudent("Asha", "Nair", "asha@smoke.dev", "Smoke Batch", "Learn Java");
            assertTrue("Add student successfully", firstStudent.getId() > 0);

            try {
                studentService.addStudent("Asha", "Duplicate", "asha@smoke.dev", "Smoke Batch", "Duplicate check");
                printFail("Reject duplicate student email", "expected DuplicateEntityException");
            } catch (DuplicateEntityException exception) {
                printPass("Reject duplicate student email");
            }

            Trainer trainer = trainerService.addTrainer("Rohit", "Mehta", "rohit@smoke.dev", "Core Java");
            assertTrue("Add trainer successfully", trainer.getId() > 0);

            Course course = courseService.addCourse("Smoke Capacity Lab", "Capacity-one smoke course.", 3, 1, CourseLevel.CORE);
            courseService.openEnrollmentWindow(course.getId());
            assertTrue("Add course with capacity 1", course.getMaxSeats() == 1);

            courseService.assignTrainer(course.getId(), trainer.getId());
            assertTrue("Assign trainer to course", course.hasTrainerAssigned());

            Student secondStudent = studentService.addStudent("Kabir", "Rao", "kabir@smoke.dev", "Smoke Batch", "Waitlist check");
            EnrollmentDecision accepted = enrollmentService.enrollStudent(firstStudent.getId(), course.getId());
            assertTrue("Enroll first student into course and verify ACTIVE accepted decision", accepted.isAccepted() && accepted.getEnrollment().getStatus() == EnrollmentStatus.ACTIVE);

            EnrollmentDecision waitlisted = enrollmentService.enrollStudent(secondStudent.getId(), course.getId());
            assertTrue("Enroll second student into same full course and verify WAITLISTED decision", waitlisted.isWaitlisted() && waitlisted.getEnrollment().getStatus() == EnrollmentStatus.WAITLISTED);

            try {
                enrollmentService.enrollStudent(secondStudent.getId(), course.getId());
                printFail("Verify duplicate open enrollment is rejected", "expected DuplicateEntityException");
            } catch (DuplicateEntityException exception) {
                printPass("Verify duplicate open enrollment is rejected");
            }

            enrollmentService.cancelEnrollment(accepted.getEnrollment().getId(), "Smoke cancellation triggers promotion");
            Enrollment promoted = enrollmentService.findEnrollmentById(waitlisted.getEnrollment().getId());
            assertTrue("Cancel first active enrollment and verify waitlisted student is promoted", promoted.getStatus() == EnrollmentStatus.ACTIVE);

            courseService.closeEnrollmentWindow(course.getId());
            Student thirdStudent = studentService.addStudent("Meera", "Iyer", "meera@smoke.dev", "Smoke Batch", "Closed window check");
            try {
                enrollmentService.enrollStudent(thirdStudent.getId(), course.getId());
                printFail("Close course enrollment window and verify new enrollment is rejected", "expected BusinessRuleViolationException");
            } catch (BusinessRuleViolationException exception) {
                printPass("Close course enrollment window and verify new enrollment is rejected");
            }

            enrollmentService.markEnrollmentCompleted(promoted.getId(), "Smoke completion");
            assertTrue("Complete enrollment and verify final status", promoted.getStatus() == EnrollmentStatus.COMPLETED);

            try {
                enrollmentService.cancelEnrollment(promoted.getId(), "Should fail");
                printFail("Verify cancelling a completed enrollment is rejected", "expected InvalidEnrollmentStateException");
            } catch (InvalidEnrollmentStateException exception) {
                printPass("Verify cancelling a completed enrollment is rejected");
            }

            ArrayList<Person> people = new ArrayList<Person>();
            people.add(firstStudent);
            people.add(trainer);
            assertTrue("Verify Person polymorphism by calling getDisplayName", people.get(0).getDisplayName().indexOf("Student #") >= 0 && people.get(1).getDisplayName().indexOf("Trainer #") >= 0);

            assertTrue("Verify action log count is greater than zero", actionLogService.count() > 0);
            assertTrue("Verify receipt count is greater than zero", receiptService.count() > 0);
            System.out.println("Rule sample: " + RuleCode.ENROLL_CAPACITY_FULL_WAITLIST.format());
            assertTrue("Verify RuleCode formatting includes LT-RULE", RuleCode.ENROLL_CAPACITY_FULL_WAITLIST.format().indexOf("LT-RULE") >= 0);
        } catch (Exception exception) {
            printFail("Unexpected smoke test interruption", exception.getMessage());
        }

        System.out.println("Smoke Test Summary: PASS=" + passed + " FAIL=" + failed);
    }

    private static void printPass(String testName) {
        passed++;
        System.out.println("[PASS] " + testName);
    }

    private static void printFail(String testName, String reason) {
        failed++;
        System.out.println("[FAIL] " + testName + " - " + reason);
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            printPass(testName);
        } else {
            printFail(testName, "condition was false");
        }
    }
}
