package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.EnrollmentDecision;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.enums.ActionType;
import com.airtribe.learntrack.entity.enums.EnrollmentStatus;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidEnrollmentStateException;
import com.airtribe.learntrack.service.policy.EnrollmentPolicy;
import com.airtribe.learntrack.util.IdGenerator;
import java.time.LocalDate;
import java.util.ArrayList;

public class EnrollmentService {
    private ArrayList<Enrollment> enrollments = new ArrayList<Enrollment>();
    private StudentService studentService;
    private CourseService courseService;
    private ActionLogService actionLogService;
    private ReceiptService receiptService;
    private EnrollmentPolicy enrollmentPolicy;

    public EnrollmentService(StudentService studentService, CourseService courseService, ActionLogService actionLogService, ReceiptService receiptService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
        this.enrollmentPolicy = new EnrollmentPolicy();
    }

    public EnrollmentDecision enrollStudent(int studentId, int courseId) throws EntityNotFoundException, DuplicateEntityException, BusinessRuleViolationException {
        Student student = studentService.findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);
        enrollmentPolicy.validateStudentCanEnroll(student);
        enrollmentPolicy.validateCourseCanAcceptEnrollment(course);
        enrollmentPolicy.validateNoOpenDuplicateEnrollment(enrollments, studentId, courseId);

        int activeSeats = countActiveSeatsForCourse(courseId);
        if (enrollmentPolicy.canOccupySeat(activeSeats, course.getMaxSeats())) {
            Enrollment enrollment = new Enrollment(
                    IdGenerator.getNextEnrollmentId(),
                    studentId,
                    courseId,
                    LocalDate.now(),
                    EnrollmentStatus.ACTIVE,
                    LocalDate.now(),
                    "Accepted through Enrollment Decision System");
            enrollments.add(enrollment);
            actionLogService.record(ActionType.ENROLLMENT_ACCEPTED, "Learner " + student.getFullName() + " accepted into " + course.getCourseCode(), "Admin", "ENROLLMENT", enrollment.getId());
            OperationReceipt receipt = receiptService.issueReceipt("ENROLL_STUDENT", "ACCEPTED", "Student #" + studentId + " accepted into " + course.getCourseCode() + ".", RuleCode.ENROLL_STUDENT_REQUIRED.format() + " | " + RuleCode.ENROLL_COURSE_REQUIRED.format(), "Monitor progress in Student Learning Trail.", "ENROLLMENT", enrollment.getId());
            return EnrollmentDecision.accepted(enrollment, receipt, "Validated learner and course; seat capacity is available.", RuleCode.ENROLL_STUDENT_REQUIRED.getCode() + " | " + RuleCode.ENROLL_COURSE_REQUIRED.getCode());
        }

        if (enrollmentPolicy.shouldWaitlist(activeSeats, course.getMaxSeats())) {
            Enrollment enrollment = new Enrollment(
                    IdGenerator.getNextEnrollmentId(),
                    studentId,
                    courseId,
                    LocalDate.now(),
                    EnrollmentStatus.WAITLISTED,
                    LocalDate.now(),
                    "Waitlisted because active seat capacity is full");
            enrollments.add(enrollment);
            int position = getWaitlistPosition(enrollment.getId());
            actionLogService.record(ActionType.ENROLLMENT_WAITLISTED, "Learner " + student.getFullName() + " waitlisted for " + course.getCourseCode(), "Admin", "ENROLLMENT", enrollment.getId());
            OperationReceipt receipt = receiptService.issueReceipt("ENROLL_STUDENT", "WAITLISTED", "Course " + course.getCourseCode() + " is full. Student was placed at waitlist position " + position + ".", RuleCode.ENROLL_CAPACITY_FULL_WAITLIST.format(), "Monitor waitlist or increase course capacity.", "ENROLLMENT", enrollment.getId());
            return EnrollmentDecision.waitlisted(enrollment, receipt, "Active seats are full; waitlist is open.", RuleCode.ENROLL_CAPACITY_FULL_WAITLIST.getCode());
        }

        OperationReceipt receipt = receiptService.issueReceipt("ENROLL_STUDENT", "REJECTED", "Enrollment decision could not be completed.", RuleCode.COURSE_CAPACITY_POSITIVE.format(), "Check course capacity.", "COURSE", courseId);
        return EnrollmentDecision.rejected(receipt, "Capacity rule blocked the decision.", RuleCode.COURSE_CAPACITY_POSITIVE.getCode());
    }

    public Enrollment findEnrollmentById(int id) throws EntityNotFoundException {
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).getId() == id) {
                return enrollments.get(i);
            }
        }
        throw new EntityNotFoundException("Enrollment #" + id + " was not found.");
    }

    public ArrayList<Enrollment> getAllEnrollments() {
        return new ArrayList<Enrollment>(enrollments);
    }

    public ArrayList<Enrollment> getOpenEnrollments() {
        ArrayList<Enrollment> open = new ArrayList<Enrollment>();
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).isOpen()) {
                open.add(enrollments.get(i));
            }
        }
        return open;
    }

    public ArrayList<Enrollment> getEnrollmentsForStudent(int studentId) {
        ArrayList<Enrollment> matches = new ArrayList<Enrollment>();
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).getStudentId() == studentId) {
                matches.add(enrollments.get(i));
            }
        }
        return matches;
    }

    public ArrayList<Enrollment> getEnrollmentsForCourse(int courseId) {
        ArrayList<Enrollment> matches = new ArrayList<Enrollment>();
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).getCourseId() == courseId) {
                matches.add(enrollments.get(i));
            }
        }
        return matches;
    }

    public ArrayList<Enrollment> getActiveEnrollmentsForCourse(int courseId) {
        ArrayList<Enrollment> matches = new ArrayList<Enrollment>();
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getCourseId() == courseId && enrollment.getStatus() == EnrollmentStatus.ACTIVE) {
                matches.add(enrollment);
            }
        }
        return matches;
    }

    public ArrayList<Enrollment> getWaitlistedEnrollmentsForCourse(int courseId) {
        ArrayList<Enrollment> matches = new ArrayList<Enrollment>();
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getCourseId() == courseId && enrollment.getStatus() == EnrollmentStatus.WAITLISTED) {
                matches.add(enrollment);
            }
        }
        return matches;
    }

    public OperationReceipt markEnrollmentCompleted(int enrollmentId, String note) throws EntityNotFoundException, InvalidEnrollmentStateException {
        Enrollment enrollment = findEnrollmentById(enrollmentId);
        enrollmentPolicy.validateTransition(enrollment, EnrollmentStatus.COMPLETED);
        enrollment.markCompleted(note == null || note.trim().length() == 0 ? "Completed from Enrollment Desk" : note.trim());
        actionLogService.record(ActionType.ENROLLMENT_COMPLETED, "Enrollment #" + enrollmentId + " completed.", "Admin", "ENROLLMENT", enrollment.getId());
        return receiptService.issueReceipt("COMPLETE_ENROLLMENT", "COMPLETED", "Enrollment #" + enrollmentId + " moved to completed final state.", RuleCode.ENROLL_FINAL_STATE.format(), "Check Student Learning Trail.", "ENROLLMENT", enrollment.getId());
    }

    public OperationReceipt cancelEnrollment(int enrollmentId, String note) throws EntityNotFoundException, InvalidEnrollmentStateException {
        Enrollment enrollment = findEnrollmentById(enrollmentId);
        EnrollmentStatus previousStatus = enrollment.getStatus();
        enrollmentPolicy.validateTransition(enrollment, EnrollmentStatus.CANCELLED);
        enrollment.markCancelled(note == null || note.trim().length() == 0 ? "Cancelled from Enrollment Desk" : note.trim());
        actionLogService.record(ActionType.ENROLLMENT_CANCELLED, "Enrollment #" + enrollmentId + " cancelled.", "Admin", "ENROLLMENT", enrollment.getId());
        OperationReceipt receipt = receiptService.issueReceipt("CANCEL_ENROLLMENT", "CANCELLED", "Enrollment #" + enrollmentId + " moved to cancelled final state.", RuleCode.ENROLL_FINAL_STATE.format(), "If this was active, waitlist promotion is checked automatically.", "ENROLLMENT", enrollment.getId());
        if (previousStatus == EnrollmentStatus.ACTIVE) {
            promoteNextWaitlistedLearner(enrollment.getCourseId());
        }
        return receipt;
    }

    public int countByStatus(EnrollmentStatus status) {
        int count = 0;
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    public int countActiveSeatsForCourse(int courseId) {
        int count = 0;
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getCourseId() == courseId && enrollment.getStatus() != null && enrollment.getStatus().occupiesSeat()) {
                count++;
            }
        }
        return count;
    }

    public int countWaitlistedForCourse(int courseId) {
        int count = 0;
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getCourseId() == courseId && enrollment.getStatus() == EnrollmentStatus.WAITLISTED) {
                count++;
            }
        }
        return count;
    }

    public boolean hasOpenEnrollment(int studentId, int courseId) {
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getStudentId() == studentId && enrollment.getCourseId() == courseId && enrollment.isOpen()) {
                return true;
            }
        }
        return false;
    }

    public Enrollment promoteNextWaitlistedLearner(int courseId) {
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getCourseId() == courseId && enrollment.getStatus() == EnrollmentStatus.WAITLISTED) {
                try {
                    enrollmentPolicy.validateTransition(enrollment, EnrollmentStatus.ACTIVE);
                    enrollment.markActive("Promoted from waitlist after active enrollment cancellation");
                    Course course = courseService.findCourseById(courseId);
                    actionLogService.record(ActionType.ENROLLMENT_PROMOTED, "Waitlisted learner promoted for " + course.getCourseCode(), "System", "ENROLLMENT", enrollment.getId());
                    receiptService.issueReceipt("PROMOTE_WAITLISTED_LEARNER", "PROMOTED", "Enrollment #" + enrollment.getId() + " was promoted from waitlist to active.", RuleCode.ENROLL_CAPACITY_FULL_WAITLIST.format(), "Notify learner and check Course Operations Card.", "ENROLLMENT", enrollment.getId());
                    return enrollment;
                } catch (Exception exception) {
                    return null;
                }
            }
        }
        return null;
    }

    public int getAvailableSeats(int courseId) throws EntityNotFoundException {
        Course course = courseService.findCourseById(courseId);
        int available = course.getMaxSeats() - countActiveSeatsForCourse(courseId);
        if (available < 0) {
            return 0;
        }
        return available;
    }

    public int getWaitlistPosition(int enrollmentId) {
        Enrollment target = null;
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).getId() == enrollmentId) {
                target = enrollments.get(i);
                break;
            }
        }
        if (target == null || target.getStatus() != EnrollmentStatus.WAITLISTED) {
            return -1;
        }
        int position = 0;
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getCourseId() == target.getCourseId() && enrollment.getStatus() == EnrollmentStatus.WAITLISTED) {
                position++;
                if (enrollment.getId() == enrollmentId) {
                    return position;
                }
            }
        }
        return -1;
    }
}
