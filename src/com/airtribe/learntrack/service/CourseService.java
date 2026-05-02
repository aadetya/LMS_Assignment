package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.entity.enums.ActionType;
import com.airtribe.learntrack.entity.enums.CourseLevel;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.policy.DeactivationPolicy;
import com.airtribe.learntrack.util.CourseCodeGenerator;
import com.airtribe.learntrack.util.IdGenerator;
import com.airtribe.learntrack.util.InputValidator;
import java.util.ArrayList;

public class CourseService {
    private ArrayList<Course> courses = new ArrayList<Course>();
    private TrainerService trainerService;
    private ActionLogService actionLogService;
    private ReceiptService receiptService;

    public CourseService(TrainerService trainerService, ActionLogService actionLogService, ReceiptService receiptService) {
        this.trainerService = trainerService;
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
    }

    public Course addCourse(String courseName, String description, int durationInWeeks, int maxSeats, CourseLevel level) throws InvalidInputException, DuplicateEntityException {
        InputValidator.requireNonBlank(courseName, "course name");
        InputValidator.requirePositiveInt(durationInWeeks, "duration in weeks");
        InputValidator.requirePositiveInt(maxSeats, "seat capacity");
        String normalizedName = InputValidator.normalizeName(courseName);
        if (activeCourseNameExists(normalizedName, 0)) {
            throw new DuplicateEntityException(RuleCode.COURSE_NAME_UNIQUE_ACTIVE.format() + " Course: " + normalizedName);
        }
        int id = IdGenerator.getNextCourseId();
        String generatedCode = CourseCodeGenerator.generateCourseCode(normalizedName, id);
        if (courseCodeExists(generatedCode, 0)) {
            throw new DuplicateEntityException(RuleCode.COURSE_CODE_UNIQUE.format() + " Code: " + generatedCode);
        }
        Course course = new Course(id, normalizedName, InputValidator.normalizeName(description), durationInWeeks, true, generatedCode, false, maxSeats, level == null ? CourseLevel.FOUNDATION : level, null);
        courses.add(course);
        actionLogService.record(ActionType.COURSE_CREATED, "Course Operations Card created for " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        receiptService.issueReceipt("CREATE_COURSE", "CREATED", "Course " + course.getCourseCode() + " entered Course Catalog Ops.", RuleCode.COURSE_NAME_UNIQUE_ACTIVE.format(), "Open Enrollment Window when ready.", "COURSE", course.getId());
        return course;
    }

    public Course addCourse(String courseName, int durationInWeeks) throws InvalidInputException, DuplicateEntityException {
        return addCourse(courseName, "Learning operations course.", durationInWeeks, 30, CourseLevel.FOUNDATION);
    }

    public ArrayList<Course> getAllCourses() {
        return new ArrayList<Course>(courses);
    }

    public ArrayList<Course> getActiveCourses() {
        ArrayList<Course> activeCourses = new ArrayList<Course>();
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).isActive()) {
                activeCourses.add(courses.get(i));
            }
        }
        return activeCourses;
    }

    public ArrayList<Course> getOpenCourses() {
        ArrayList<Course> openCourses = new ArrayList<Course>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.isActive() && course.isEnrollmentOpen()) {
                openCourses.add(course);
            }
        }
        return openCourses;
    }

    public Course findCourseById(int id) throws EntityNotFoundException {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId() == id) {
                return courses.get(i);
            }
        }
        throw new EntityNotFoundException(RuleCode.ENROLL_COURSE_REQUIRED.format() + " Course #" + id + " was not found.");
    }

    public Course findCourseByCode(String courseCode) throws EntityNotFoundException {
        String normalizedCode = InputValidator.normalizeCode(courseCode);
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseCode() != null && courses.get(i).getCourseCode().equalsIgnoreCase(normalizedCode)) {
                return courses.get(i);
            }
        }
        throw new EntityNotFoundException("Course code not found: " + normalizedCode);
    }

    public ArrayList<Course> searchCoursesByName(String keyword) {
        ArrayList<Course> matches = new ArrayList<Course>();
        String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseName().toLowerCase().indexOf(normalized) >= 0) {
                matches.add(courses.get(i));
            }
        }
        return matches;
    }

    public Course updateCourse(int id, String courseName, String description, int durationInWeeks, int maxSeats, CourseLevel level) throws EntityNotFoundException, InvalidInputException, DuplicateEntityException {
        Course course = findCourseById(id);
        InputValidator.requireNonBlank(courseName, "course name");
        InputValidator.requirePositiveInt(durationInWeeks, "duration in weeks");
        InputValidator.requirePositiveInt(maxSeats, "seat capacity");
        String normalizedName = InputValidator.normalizeName(courseName);
        if (course.isActive() && activeCourseNameExists(normalizedName, id)) {
            throw new DuplicateEntityException(RuleCode.COURSE_NAME_UNIQUE_ACTIVE.format() + " Course: " + normalizedName);
        }
        String generatedCode = CourseCodeGenerator.generateCourseCode(normalizedName, id);
        if (courseCodeExists(generatedCode, id)) {
            throw new DuplicateEntityException(RuleCode.COURSE_CODE_UNIQUE.format() + " Code: " + generatedCode);
        }
        course.setCourseName(normalizedName);
        course.setDescription(InputValidator.normalizeName(description));
        course.setDurationInWeeks(durationInWeeks);
        course.setMaxSeats(maxSeats);
        course.setLevel(level == null ? CourseLevel.FOUNDATION : level);
        course.setCourseCode(generatedCode);
        actionLogService.record(ActionType.COURSE_UPDATED, "Course updated for " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        receiptService.issueReceipt("UPDATE_COURSE", "UPDATED", "Course Operations Card refreshed for " + course.getCourseCode() + ".", RuleCode.COURSE_CAPACITY_POSITIVE.format(), "Check capacity and waitlist reports.", "COURSE", course.getId());
        return course;
    }

    public OperationReceipt deactivateCourse(int id, ArrayList<Enrollment> enrollments) throws EntityNotFoundException, BusinessRuleViolationException {
        Course course = findCourseById(id);
        new DeactivationPolicy().validateCourseCanBeDeactivated(course, enrollments);
        course.setActive(false);
        course.setEnrollmentOpen(false);
        actionLogService.record(ActionType.COURSE_DEACTIVATED, "Safe Deactivation completed for " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        return receiptService.issueReceipt("SAFE_DEACTIVATE_COURSE", "DEACTIVATED", "Course deactivated and Enrollment Window closed.", RuleCode.DEACTIVATE_COURSE_HAS_ACTIVE_ENROLLMENTS.format(), "Reactivate before reopening enrollment.", "COURSE", course.getId());
    }

    public OperationReceipt reactivateCourse(int id) throws EntityNotFoundException {
        Course course = findCourseById(id);
        course.setActive(true);
        actionLogService.record(ActionType.COURSE_REACTIVATED, "Course reactivated for " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        return receiptService.issueReceipt("REACTIVATE_COURSE", "REACTIVATED", "Course returned to active catalog status.", RuleCode.COURSE_INACTIVE.format(), "Open Enrollment Window when admissions should resume.", "COURSE", course.getId());
    }

    public OperationReceipt openEnrollmentWindow(int courseId) throws EntityNotFoundException, BusinessRuleViolationException {
        Course course = findCourseById(courseId);
        if (!course.isActive()) {
            throw new BusinessRuleViolationException(RuleCode.COURSE_INACTIVE.format() + " Course #" + courseId + " cannot open enrollment.");
        }
        course.setEnrollmentOpen(true);
        actionLogService.record(ActionType.ENROLLMENT_OPENED, "Enrollment Window opened for " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        return receiptService.issueReceipt("OPEN_ENROLLMENT_WINDOW", "OPENED", "Enrollment Window opened for " + course.getCourseCode() + ".", RuleCode.COURSE_ENROLLMENT_CLOSED.format(), "Run enrollment decisions from Enrollment Desk.", "COURSE", course.getId());
    }

    public OperationReceipt closeEnrollmentWindow(int courseId) throws EntityNotFoundException {
        Course course = findCourseById(courseId);
        course.setEnrollmentOpen(false);
        actionLogService.record(ActionType.ENROLLMENT_CLOSED, "Enrollment Window closed for " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        return receiptService.issueReceipt("CLOSE_ENROLLMENT_WINDOW", "CLOSED", "Enrollment Window closed for " + course.getCourseCode() + ".", RuleCode.COURSE_ENROLLMENT_CLOSED.format(), "Existing enrollments remain visible in reports.", "COURSE", course.getId());
    }

    public OperationReceipt assignTrainer(int courseId, int trainerId) throws EntityNotFoundException, BusinessRuleViolationException {
        Course course = findCourseById(courseId);
        Trainer trainer = trainerService.findTrainerById(trainerId);
        if (!trainer.isActive()) {
            throw new BusinessRuleViolationException(RuleCode.TRAINER_INACTIVE_ASSIGNMENT.format() + " Trainer #" + trainerId + " is inactive.");
        }
        course.setTrainerId(Integer.valueOf(trainerId));
        actionLogService.record(ActionType.TRAINER_ASSIGNED, "Trainer " + trainer.getFullName() + " assigned to " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        return receiptService.issueReceipt("ASSIGN_TRAINER", "ASSIGNED", "Trainer " + trainer.getFullName() + " now covers " + course.getCourseCode() + ".", RuleCode.TRAINER_INACTIVE_ASSIGNMENT.format(), "Check Trainer Coverage Report.", "COURSE", course.getId());
    }

    public OperationReceipt unassignTrainer(int courseId) throws EntityNotFoundException {
        Course course = findCourseById(courseId);
        course.setTrainerId(null);
        actionLogService.record(ActionType.TRAINER_UNASSIGNED, "Trainer unassigned from " + course.getCourseCode(), "Admin", "COURSE", course.getId());
        return receiptService.issueReceipt("UNASSIGN_TRAINER", "UNASSIGNED", "Course " + course.getCourseCode() + " no longer has a trainer assigned.", RuleCode.DEACTIVATE_TRAINER_ASSIGNED_TO_ACTIVE_COURSE.format(), "Assign a trainer before running a cohort.", "COURSE", course.getId());
    }

    public int countActiveCourses() {
        return getActiveCourses().size();
    }

    public int countInactiveCourses() {
        int count = 0;
        for (int i = 0; i < courses.size(); i++) {
            if (!courses.get(i).isActive()) {
                count++;
            }
        }
        return count;
    }

    public int countOpenCourses() {
        return getOpenCourses().size();
    }

    public int countClosedCourses() {
        int count = 0;
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.isActive() && !course.isEnrollmentOpen()) {
                count++;
            }
        }
        return count;
    }

    public boolean activeCourseNameExists(String courseName, int currentCourseId) {
        String normalized = InputValidator.normalizeName(courseName).toLowerCase();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getId() != currentCourseId && course.isActive() && course.getCourseName() != null && course.getCourseName().toLowerCase().equals(normalized)) {
                return true;
            }
        }
        return false;
    }

    public boolean courseCodeExists(String courseCode, int currentCourseId) {
        String normalized = InputValidator.normalizeCode(courseCode);
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getId() != currentCourseId && course.getCourseCode() != null && course.getCourseCode().equalsIgnoreCase(normalized)) {
                return true;
            }
        }
        return false;
    }
}
