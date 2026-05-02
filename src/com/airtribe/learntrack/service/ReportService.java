package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Person;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.entity.enums.EnrollmentStatus;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.TextTablePrinter;
import java.util.ArrayList;

public class ReportService {
    private StudentService studentService;
    private TrainerService trainerService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private ActionLogService actionLogService;
    private ReceiptService receiptService;

    public ReportService(StudentService studentService, TrainerService trainerService, CourseService courseService, EnrollmentService enrollmentService, ActionLogService actionLogService, ReceiptService receiptService) {
        this.studentService = studentService;
        this.trainerService = trainerService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
    }

    public void printLearningPulseDashboard() {
        ConsolePrinter.printHeader("Learning Pulse Dashboard");
        ConsolePrinter.printKeyValue("Total students", String.valueOf(studentService.getAllStudents().size()));
        ConsolePrinter.printKeyValue("Active students", String.valueOf(studentService.countActiveStudents()));
        ConsolePrinter.printKeyValue("Inactive students", String.valueOf(studentService.countInactiveStudents()));
        ConsolePrinter.printKeyValue("Total trainers", String.valueOf(trainerService.getAllTrainers().size()));
        ConsolePrinter.printKeyValue("Active trainers", String.valueOf(trainerService.countActiveTrainers()));
        ConsolePrinter.printKeyValue("Inactive trainers", String.valueOf(trainerService.countInactiveTrainers()));
        ConsolePrinter.printKeyValue("Total courses", String.valueOf(courseService.getAllCourses().size()));
        ConsolePrinter.printKeyValue("Active courses", String.valueOf(courseService.countActiveCourses()));
        ConsolePrinter.printKeyValue("Inactive courses", String.valueOf(courseService.countInactiveCourses()));
        ConsolePrinter.printKeyValue("Open courses", String.valueOf(courseService.countOpenCourses()));
        ConsolePrinter.printKeyValue("Closed courses", String.valueOf(courseService.countClosedCourses()));
        ConsolePrinter.printKeyValue("Total enrollments", String.valueOf(enrollmentService.getAllEnrollments().size()));
        ConsolePrinter.printKeyValue("Active enrollments", String.valueOf(enrollmentService.countByStatus(EnrollmentStatus.ACTIVE)));
        ConsolePrinter.printKeyValue("Waitlisted enrollments", String.valueOf(enrollmentService.countByStatus(EnrollmentStatus.WAITLISTED)));
        ConsolePrinter.printKeyValue("Completed enrollments", String.valueOf(enrollmentService.countByStatus(EnrollmentStatus.COMPLETED)));
        ConsolePrinter.printKeyValue("Cancelled enrollments", String.valueOf(enrollmentService.countByStatus(EnrollmentStatus.CANCELLED)));
        ConsolePrinter.printKeyValue("Action journal entries", String.valueOf(actionLogService.count()));
        ConsolePrinter.printKeyValue("Operation receipts", String.valueOf(receiptService.count()));
    }

    public void printCohortHealthScore() {
        ConsolePrinter.printHeader("Cohort Health Score");
        int score = 100;
        ArrayList<String> signals = new ArrayList<String>();
        ArrayList<Course> courses = courseService.getAllCourses();
        boolean activeCourseWithoutTrainer = false;
        boolean activeCourseOverNinety = false;
        boolean openCourseWithZeroActive = false;
        int totalWaitlist = 0;
        int totalAvailableSeats = 0;

        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.isActive()) {
                if (!course.hasTrainerAssigned()) {
                    activeCourseWithoutTrainer = true;
                }
                int activeSeats = enrollmentService.countActiveSeatsForCourse(course.getId());
                int utilization = calculateUtilizationPercentage(activeSeats, course.getMaxSeats());
                if (utilization > 90) {
                    activeCourseOverNinety = true;
                }
                try {
                    totalAvailableSeats = totalAvailableSeats + enrollmentService.getAvailableSeats(course.getId());
                } catch (Exception exception) {
                    totalAvailableSeats = totalAvailableSeats + 0;
                }
                totalWaitlist = totalWaitlist + enrollmentService.countWaitlistedForCourse(course.getId());
                if (course.isEnrollmentOpen() && activeSeats == 0) {
                    openCourseWithZeroActive = true;
                }
            }
        }

        if (activeCourseWithoutTrainer) {
            score = score - 10;
            signals.add("-10: At least one active course has no trainer.");
        }
        if (activeCourseOverNinety) {
            score = score - 10;
            signals.add("-10: At least one active course is over 90% full.");
        }
        if (totalWaitlist > totalAvailableSeats) {
            score = score - 10;
            signals.add("-10: Total waitlist count is greater than total available seats.");
        }
        if (hasInactiveStudentWithOpenEnrollment()) {
            score = score - 5;
            signals.add("-5: Inactive students have open enrollments.");
        }
        if (openCourseWithZeroActive) {
            score = score - 5;
            signals.add("-5: Open courses exist with zero active enrollments.");
        }
        if (score < 0) {
            score = 0;
        }

        String label = "AT RISK";
        if (score >= 85) {
            label = "HEALTHY";
        } else if (score >= 65) {
            label = "NEEDS ATTENTION";
        }
        ConsolePrinter.printKeyValue("Score", score + "/100");
        ConsolePrinter.printKeyValue("Label", label);
        if (signals.size() == 0) {
            ConsolePrinter.printSuccess("No negative cohort health signals found.");
        } else {
            ConsolePrinter.printSubHeader("Signals");
            for (int i = 0; i < signals.size(); i++) {
                System.out.println(signals.get(i));
            }
        }
    }

    public void printCourseWiseEnrollmentCounts() {
        ConsolePrinter.printHeader("Course-wise Enrollment Counts");
        ArrayList<Course> courses = courseService.getAllCourses();
        if (courses.size() == 0) {
            ConsolePrinter.printEmpty("courses");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            rows.add(new String[] {
                    String.valueOf(course.getId()),
                    course.getCourseCode(),
                    course.getCourseName(),
                    String.valueOf(enrollmentService.getActiveEnrollmentsForCourse(course.getId()).size()),
                    String.valueOf(enrollmentService.countWaitlistedForCourse(course.getId())),
                    String.valueOf(countCourseStatus(course.getId(), EnrollmentStatus.COMPLETED)),
                    String.valueOf(countCourseStatus(course.getId(), EnrollmentStatus.CANCELLED))
            });
        }
        TextTablePrinter.printTable(new String[] {"ID", "Code", "Course", "Active", "Waitlist", "Complete", "Cancel"}, new int[] {6, 14, 30, 8, 8, 8, 8}, rows);
    }

    public void printCourseOperationsCard(int courseId) {
        ConsolePrinter.printHeader("Course Operations Card");
        try {
            Course course = courseService.findCourseById(courseId);
            int activeSeats = enrollmentService.countActiveSeatsForCourse(courseId);
            int waitlistCount = enrollmentService.countWaitlistedForCourse(courseId);
            int completedCount = countCourseStatus(courseId, EnrollmentStatus.COMPLETED);
            int cancelledCount = countCourseStatus(courseId, EnrollmentStatus.CANCELLED);
            int availableSeats = enrollmentService.getAvailableSeats(courseId);
            int utilization = calculateUtilizationPercentage(activeSeats, course.getMaxSeats());
            ConsolePrinter.printKeyValue("Course ID", String.valueOf(course.getId()));
            ConsolePrinter.printKeyValue("Course Code", course.getCourseCode());
            ConsolePrinter.printKeyValue("Course Name", course.getCourseName());
            ConsolePrinter.printKeyValue("Level", course.getLevel().getDisplayText());
            ConsolePrinter.printKeyValue("Trainer", resolveTrainerName(course));
            ConsolePrinter.printKeyValue("Catalog State", course.isActive() ? "Active" : "Inactive");
            ConsolePrinter.printKeyValue("Enrollment Window", course.isEnrollmentOpen() ? "Open" : "Closed");
            ConsolePrinter.printKeyValue("Max Seats", String.valueOf(course.getMaxSeats()));
            ConsolePrinter.printKeyValue("Active Seats Taken", String.valueOf(activeSeats));
            ConsolePrinter.printKeyValue("Available Seats", String.valueOf(availableSeats));
            ConsolePrinter.printKeyValue("Waitlist Count", String.valueOf(waitlistCount));
            ConsolePrinter.printKeyValue("Completed Count", String.valueOf(completedCount));
            ConsolePrinter.printKeyValue("Cancelled Count", String.valueOf(cancelledCount));
            ConsolePrinter.printKeyValue("Utilization", utilization + "%");
            ConsolePrinter.printKeyValue("Operational Label", getOperationalLabel(activeSeats, course.getMaxSeats(), waitlistCount));
            ConsolePrinter.printKeyValue("Recommended Action", getRecommendedCourseAction(course, activeSeats, waitlistCount));
        } catch (Exception exception) {
            ConsolePrinter.printError(exception.getMessage());
        }
    }

    public void printStudentLearningTrail(int studentId) {
        ConsolePrinter.printHeader("Student Learning Trail");
        try {
            Student student = studentService.findStudentById(studentId);
            ConsolePrinter.printKeyValue("Student", "#" + student.getId() + " - " + student.getFullName());
            ConsolePrinter.printKeyValue("Batch", student.getBatch());
            ConsolePrinter.printKeyValue("State", student.isActive() ? "Active" : "Inactive");
            ConsolePrinter.printKeyValue("Learning Goal", student.getLearningGoal());
            ConsolePrinter.printKeyValue("Joined", String.valueOf(student.getJoinedOn()));
            ArrayList<Enrollment> studentEnrollments = enrollmentService.getEnrollmentsForStudent(studentId);
            if (studentEnrollments.size() == 0) {
                ConsolePrinter.printEmpty("learning trail entries");
                return;
            }
            ConsolePrinter.printSubHeader("Trail Entries");
            ArrayList<String[]> trailRows = new ArrayList<String[]>();
            for (int i = 0; i < studentEnrollments.size(); i++) {
                Enrollment enrollment = studentEnrollments.get(i);
                String courseLine = "Course #" + enrollment.getCourseId();
                try {
                    Course course = courseService.findCourseById(enrollment.getCourseId());
                    courseLine = course.getCourseCode() + " - " + course.getCourseName();
                } catch (Exception exception) {
                    courseLine = "Course #" + enrollment.getCourseId();
                }
                trailRows.add(new String[] {
                        enrollment.getEnrollmentDate() + " -> " + enrollment.getLastStatusChangedOn(),
                        courseLine,
                        enrollment.getStatus().getDisplayText(),
                        enrollment.getStatusNote()
                });
            }
            TextTablePrinter.printTable(new String[] {"Dates", "Course", "Status", "Note"}, new int[] {23, 34, 12, 34}, trailRows);
            ConsolePrinter.printSubHeader("Grouped Signals");
            printStudentStatusGroup(studentEnrollments, EnrollmentStatus.ACTIVE, "Active courses");
            printStudentStatusGroup(studentEnrollments, EnrollmentStatus.WAITLISTED, "Waitlisted courses");
            printStudentStatusGroup(studentEnrollments, EnrollmentStatus.COMPLETED, "Completed courses");
            printStudentStatusGroup(studentEnrollments, EnrollmentStatus.CANCELLED, "Cancelled enrollments");
        } catch (Exception exception) {
            ConsolePrinter.printError(exception.getMessage());
        }
    }

    public void printWaitlistReport() {
        ConsolePrinter.printHeader("Waitlist Report");
        ArrayList<Course> courses = courseService.getAllCourses();
        boolean anyWaitlist = false;
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            ArrayList<Enrollment> waitlisted = enrollmentService.getWaitlistedEnrollmentsForCourse(course.getId());
            if (waitlisted.size() > 0) {
                anyWaitlist = true;
                ConsolePrinter.printSubHeader(course.getCourseCode() + " - " + course.getCourseName());
                for (int j = 0; j < waitlisted.size(); j++) {
                    Enrollment enrollment = waitlisted.get(j);
                    String name = "Student #" + enrollment.getStudentId();
                    try {
                        name = studentService.findStudentById(enrollment.getStudentId()).getFullName();
                    } catch (Exception exception) {
                        name = "Student #" + enrollment.getStudentId();
                    }
                    System.out.println("Position " + (j + 1) + " | Student #" + enrollment.getStudentId() + " | " + name);
                }
            }
        }
        if (!anyWaitlist) {
            ConsolePrinter.printEmpty("waitlisted learners");
        }
    }

    public void printCapacityReport() {
        ConsolePrinter.printHeader("Capacity Report");
        ArrayList<Course> courses = courseService.getActiveCourses();
        if (courses.size() == 0) {
            ConsolePrinter.printEmpty("active courses");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            int activeSeats = enrollmentService.countActiveSeatsForCourse(course.getId());
            int availableSeats = course.getMaxSeats() - activeSeats;
            if (availableSeats < 0) {
                availableSeats = 0;
            }
            int utilization = calculateUtilizationPercentage(activeSeats, course.getMaxSeats());
            String label = "HEALTHY";
            if (activeSeats == 0) {
                label = "LOW";
            } else if (utilization >= 100) {
                label = "FULL";
            }
            rows.add(new String[] {
                    String.valueOf(course.getId()),
                    course.getCourseCode(),
                    course.getCourseName(),
                    String.valueOf(course.getMaxSeats()),
                    String.valueOf(activeSeats),
                    String.valueOf(availableSeats),
                    utilization + "%",
                    label
            });
        }
        TextTablePrinter.printTable(new String[] {"ID", "Code", "Course", "Max", "Active", "Available", "Use", "Label"}, new int[] {6, 14, 30, 5, 6, 9, 6, 10}, rows);
    }

    public void printTrainerCoverageReport() {
        ConsolePrinter.printHeader("Trainer Coverage Report");
        ArrayList<Course> courses = courseService.getActiveCourses();
        ConsolePrinter.printSubHeader("Courses with trainers");
        boolean withTrainer = false;
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.hasTrainerAssigned()) {
                withTrainer = true;
                System.out.println(course.getCourseCode() + " | " + course.getCourseName() + " | " + resolveTrainerName(course));
            }
        }
        if (!withTrainer) {
            ConsolePrinter.printEmpty("covered courses");
        }
        ConsolePrinter.printSubHeader("Courses without trainers");
        boolean withoutTrainer = false;
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (!course.hasTrainerAssigned()) {
                withoutTrainer = true;
                System.out.println(course.getCourseCode() + " | " + course.getCourseName());
            }
        }
        if (!withoutTrainer) {
            ConsolePrinter.printEmpty("uncovered active courses");
        }
        ConsolePrinter.printSubHeader("Active trainers not assigned to an active course");
        ArrayList<Trainer> trainers = trainerService.getActiveTrainers();
        boolean freeTrainer = false;
        for (int i = 0; i < trainers.size(); i++) {
            Trainer trainer = trainers.get(i);
            if (!isTrainerAssignedToActiveCourse(trainer.getId(), courses)) {
                freeTrainer = true;
                System.out.println(trainer.getTrainerCard());
            }
        }
        if (!freeTrainer) {
            ConsolePrinter.printEmpty("unassigned active trainers");
        }
    }

    public void printActionJournal(int recentLimit) {
        ConsolePrinter.printHeader("Action Journal");
        if (recentLimit > 0) {
            TextTablePrinter.printActionLog(actionLogService.getRecentEntries(recentLimit));
        } else {
            TextTablePrinter.printActionLog(actionLogService.getAllEntries());
        }
    }

    public void printRecentReceipts(int recentLimit) {
        ConsolePrinter.printHeader("Operation Receipts");
        if (recentLimit > 0) {
            TextTablePrinter.printReceipts(receiptService.getRecentReceipts(recentLimit));
        } else {
            TextTablePrinter.printReceipts(receiptService.getAllReceipts());
        }
    }

    public void printReceiptsForReference(String referenceType, int referenceId) {
        ConsolePrinter.printHeader("Receipts for " + referenceType + " #" + referenceId);
        TextTablePrinter.printReceipts(receiptService.findReceiptsByReference(referenceType, referenceId));
    }

    public void printPeopleDirectory() {
        ConsolePrinter.printHeader("People Directory");
        ArrayList<Person> people = new ArrayList<Person>();
        ArrayList<Person> students = studentService.getStudentsAsPeople();
        ArrayList<Person> trainers = trainerService.getTrainersAsPeople();
        for (int i = 0; i < students.size(); i++) {
            people.add(students.get(i));
        }
        for (int i = 0; i < trainers.size(); i++) {
            people.add(trainers.get(i));
        }
        TextTablePrinter.printPeople(people);
    }

    private int countCourseStatus(int courseId, EnrollmentStatus status) {
        int count = 0;
        ArrayList<Enrollment> courseEnrollments = enrollmentService.getEnrollmentsForCourse(courseId);
        for (int i = 0; i < courseEnrollments.size(); i++) {
            if (courseEnrollments.get(i).getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    private int calculateUtilizationPercentage(int activeSeats, int maxSeats) {
        if (maxSeats <= 0) {
            return 0;
        }
        return (activeSeats * 100) / maxSeats;
    }

    private boolean hasInactiveStudentWithOpenEnrollment() {
        ArrayList<Student> students = studentService.getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (!student.isActive()) {
                ArrayList<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(student.getId());
                for (int j = 0; j < enrollments.size(); j++) {
                    if (enrollments.get(j).isOpen()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String resolveTrainerName(Course course) {
        if (!course.hasTrainerAssigned()) {
            return "Unassigned";
        }
        try {
            Trainer trainer = trainerService.findTrainerById(course.getTrainerId().intValue());
            return trainer.getFullName();
        } catch (Exception exception) {
            return "Trainer #" + course.getTrainerId();
        }
    }

    private String getOperationalLabel(int activeSeats, int maxSeats, int waitlistCount) {
        if (waitlistCount > 0) {
            return "WAITLIST ACTIVE";
        }
        if (activeSeats == 0) {
            return "EMPTY";
        }
        if (activeSeats >= maxSeats) {
            return "FULL";
        }
        int utilization = calculateUtilizationPercentage(activeSeats, maxSeats);
        if (utilization >= 90) {
            return "NEAR CAPACITY";
        }
        return "FILLING";
    }

    private String getRecommendedCourseAction(Course course, int activeSeats, int waitlistCount) {
        if (!course.hasTrainerAssigned()) {
            return "Assign trainer";
        }
        if (course.isActive() && !course.isEnrollmentOpen()) {
            return "Open enrollment";
        }
        if (waitlistCount > 0) {
            return "Check waitlist";
        }
        if (activeSeats >= course.getMaxSeats()) {
            return "Increase capacity";
        }
        return "No action needed";
    }

    private void printStudentStatusGroup(ArrayList<Enrollment> enrollments, EnrollmentStatus status, String label) {
        System.out.println(label + ":");
        boolean found = false;
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getStatus() == status) {
                found = true;
                System.out.println("  Enrollment #" + enrollment.getId() + " | Course #" + enrollment.getCourseId() + " | " + enrollment.getStatusNote());
            }
        }
        if (!found) {
            System.out.println("  none");
        }
    }

    private boolean isTrainerAssignedToActiveCourse(int trainerId, ArrayList<Course> courses) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.hasTrainerAssigned() && course.getTrainerId().intValue() == trainerId) {
                return true;
            }
        }
        return false;
    }
}
