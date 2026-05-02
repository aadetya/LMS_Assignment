package com.airtribe.learntrack.service.policy;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.enums.EnrollmentStatus;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.InvalidEnrollmentStateException;
import java.util.ArrayList;

public class EnrollmentPolicy {
    public void validateStudentCanEnroll(Student student) throws BusinessRuleViolationException {
        if (student == null || !student.isActive()) {
            throw new BusinessRuleViolationException(RuleCode.ENROLL_INACTIVE_STUDENT.format() + " Student must be active before an enrollment decision.");
        }
    }

    public void validateCourseCanAcceptEnrollment(Course course) throws BusinessRuleViolationException {
        if (course == null || !course.isActive()) {
            throw new BusinessRuleViolationException(RuleCode.COURSE_INACTIVE.format() + " Course must be active.");
        }
        if (!course.isEnrollmentOpen()) {
            throw new BusinessRuleViolationException(RuleCode.COURSE_ENROLLMENT_CLOSED.format() + " Open the Enrollment Window first.");
        }
    }

    public void validateNoOpenDuplicateEnrollment(ArrayList<Enrollment> enrollments, int studentId, int courseId) throws DuplicateEntityException {
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getStudentId() == studentId && enrollment.getCourseId() == courseId && enrollment.isOpen()) {
                throw new DuplicateEntityException(RuleCode.ENROLL_DUPLICATE_OPEN.format() + " Student #" + studentId + " is already open in Course #" + courseId + ".");
            }
        }
    }

    public void validateTransition(Enrollment enrollment, EnrollmentStatus targetStatus) throws InvalidEnrollmentStateException {
        if (enrollment == null || targetStatus == null) {
            throw new InvalidEnrollmentStateException(RuleCode.ENROLL_FINAL_STATE.format() + " Enrollment and target status are required.");
        }
        EnrollmentStatus current = enrollment.getStatus();
        if (current == targetStatus) {
            return;
        }
        if (enrollment.isFinal()) {
            throw new InvalidEnrollmentStateException(RuleCode.ENROLL_FINAL_STATE.format() + " Enrollment #" + enrollment.getId() + " is already final.");
        }
        if (current == EnrollmentStatus.WAITLISTED && (targetStatus == EnrollmentStatus.ACTIVE || targetStatus == EnrollmentStatus.CANCELLED)) {
            return;
        }
        if (current == EnrollmentStatus.ACTIVE && (targetStatus == EnrollmentStatus.COMPLETED || targetStatus == EnrollmentStatus.CANCELLED)) {
            return;
        }
        throw new InvalidEnrollmentStateException(RuleCode.ENROLL_FINAL_STATE.format() + " Cannot move enrollment #" + enrollment.getId() + " from " + current.getDisplayText() + " to " + targetStatus.getDisplayText() + ".");
    }

    public boolean canOccupySeat(int activeSeatCount, int maxSeats) {
        return activeSeatCount < maxSeats;
    }

    public boolean shouldWaitlist(int activeSeatCount, int maxSeats) {
        return activeSeatCount >= maxSeats;
    }
}
