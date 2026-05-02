package com.airtribe.learntrack.service.policy;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.entity.enums.EnrollmentStatus;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import java.util.ArrayList;

public class DeactivationPolicy {
    public void validateStudentCanBeDeactivated(Student student, ArrayList<Enrollment> enrollments) throws BusinessRuleViolationException {
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getStudentId() == student.getId() && enrollment.isOpen()) {
                throw new BusinessRuleViolationException(RuleCode.DEACTIVATE_STUDENT_HAS_OPEN_ENROLLMENTS.format() + " Cancel or complete enrollment #" + enrollment.getId() + " before Safe Deactivation.");
            }
        }
    }

    public void validateCourseCanBeDeactivated(Course course, ArrayList<Enrollment> enrollments) throws BusinessRuleViolationException {
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getCourseId() == course.getId() && enrollment.getStatus() == EnrollmentStatus.ACTIVE) {
                throw new BusinessRuleViolationException(RuleCode.DEACTIVATE_COURSE_HAS_ACTIVE_ENROLLMENTS.format() + " Resolve active enrollment #" + enrollment.getId() + " first.");
            }
        }
    }

    public void validateTrainerCanBeDeactivated(Trainer trainer, ArrayList<Course> courses) throws BusinessRuleViolationException {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.isActive() && course.hasTrainerAssigned() && course.getTrainerId().intValue() == trainer.getId()) {
                throw new BusinessRuleViolationException(RuleCode.DEACTIVATE_TRAINER_ASSIGNED_TO_ACTIVE_COURSE.format() + " Unassign trainer from Course #" + course.getId() + " first.");
            }
        }
    }
}
