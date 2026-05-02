package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Person;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.enums.ActionType;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.policy.DeactivationPolicy;
import com.airtribe.learntrack.util.IdGenerator;
import com.airtribe.learntrack.util.InputValidator;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentService {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ActionLogService actionLogService;
    private ReceiptService receiptService;

    public StudentService(ActionLogService actionLogService, ReceiptService receiptService) {
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
    }

    public Student addStudent(String firstName, String lastName, String email, String batch, String learningGoal) throws InvalidInputException, DuplicateEntityException {
        InputValidator.requireNonBlank(firstName, "student first name");
        InputValidator.requireNonBlank(lastName, "student last name");
        InputValidator.requireNonBlank(batch, "batch");
        InputValidator.validateEmailIfPresent(email);
        String normalizedEmail = InputValidator.normalizeEmail(email);
        if (normalizedEmail.length() > 0 && emailExistsForAnotherStudent(normalizedEmail, 0)) {
            throw new DuplicateEntityException(RuleCode.STUDENT_EMAIL_UNIQUE.format() + " Email: " + normalizedEmail);
        }
        Student student = new Student(
                IdGenerator.getNextStudentId(),
                InputValidator.normalizeName(firstName),
                InputValidator.normalizeName(lastName),
                normalizedEmail,
                InputValidator.normalizeName(batch),
                true,
                InputValidator.normalizeName(learningGoal),
                LocalDate.now());
        students.add(student);
        actionLogService.record(ActionType.STUDENT_CREATED, "Learner profile created for " + student.getFullName(), "Admin", "STUDENT", student.getId());
        receiptService.issueReceipt("CREATE_STUDENT", "CREATED", "Learner " + student.getFullName() + " joined the Learner Desk.", RuleCode.STUDENT_EMAIL_UNIQUE.format(), "Run an enrollment decision when ready.", "STUDENT", student.getId());
        return student;
    }

    public Student addStudent(String firstName, String lastName, String batch) throws InvalidInputException, DuplicateEntityException {
        return addStudent(firstName, lastName, "", batch, "");
    }

    public ArrayList<Student> getAllStudents() {
        return new ArrayList<Student>(students);
    }

    public ArrayList<Student> getActiveStudents() {
        ArrayList<Student> activeStudents = new ArrayList<Student>();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isActive()) {
                activeStudents.add(students.get(i));
            }
        }
        return activeStudents;
    }

    public Student findStudentById(int id) throws EntityNotFoundException {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                return students.get(i);
            }
        }
        throw new EntityNotFoundException(RuleCode.ENROLL_STUDENT_REQUIRED.format() + " Student #" + id + " was not found.");
    }

    public Student findStudentByEmail(String email) throws EntityNotFoundException {
        String normalizedEmail = InputValidator.normalizeEmail(email);
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getEmail() != null && students.get(i).getEmail().equalsIgnoreCase(normalizedEmail)) {
                return students.get(i);
            }
        }
        throw new EntityNotFoundException("Student email not found: " + normalizedEmail);
    }

    public ArrayList<Student> searchStudentsByName(String keyword) {
        ArrayList<Student> matches = new ArrayList<Student>();
        String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getFullName().toLowerCase().indexOf(normalized) >= 0) {
                matches.add(students.get(i));
            }
        }
        return matches;
    }

    public Student updateStudent(int id, String firstName, String lastName, String email, String batch, String learningGoal) throws EntityNotFoundException, InvalidInputException, DuplicateEntityException {
        Student student = findStudentById(id);
        InputValidator.requireNonBlank(firstName, "student first name");
        InputValidator.requireNonBlank(lastName, "student last name");
        InputValidator.requireNonBlank(batch, "batch");
        InputValidator.validateEmailIfPresent(email);
        String normalizedEmail = InputValidator.normalizeEmail(email);
        if (normalizedEmail.length() > 0 && emailExistsForAnotherStudent(normalizedEmail, id)) {
            throw new DuplicateEntityException(RuleCode.STUDENT_EMAIL_UNIQUE.format() + " Email: " + normalizedEmail);
        }
        student.setFirstName(InputValidator.normalizeName(firstName));
        student.setLastName(InputValidator.normalizeName(lastName));
        student.setEmail(normalizedEmail);
        student.setBatch(InputValidator.normalizeName(batch));
        student.setLearningGoal(InputValidator.normalizeName(learningGoal));
        actionLogService.record(ActionType.STUDENT_UPDATED, "Learner profile updated for " + student.getFullName(), "Admin", "STUDENT", student.getId());
        receiptService.issueReceipt("UPDATE_STUDENT", "UPDATED", "Learner profile refreshed for " + student.getFullName() + ".", RuleCode.STUDENT_EMAIL_UNIQUE.format(), "Check Student Learning Trail for current operations.", "STUDENT", student.getId());
        return student;
    }

    public OperationReceipt deactivateStudent(int id, ArrayList<Enrollment> enrollments) throws EntityNotFoundException, BusinessRuleViolationException {
        Student student = findStudentById(id);
        new DeactivationPolicy().validateStudentCanBeDeactivated(student, enrollments);
        student.setActive(false);
        actionLogService.record(ActionType.STUDENT_DEACTIVATED, "Safe Deactivation completed for " + student.getFullName(), "Admin", "STUDENT", student.getId());
        return receiptService.issueReceipt("SAFE_DEACTIVATE_STUDENT", "DEACTIVATED", "Learner was deactivated without erasing the Student Learning Trail.", RuleCode.DEACTIVATE_STUDENT_HAS_OPEN_ENROLLMENTS.format(), "Reactivate if the learner returns.", "STUDENT", student.getId());
    }

    public OperationReceipt reactivateStudent(int id) throws EntityNotFoundException {
        Student student = findStudentById(id);
        student.setActive(true);
        actionLogService.record(ActionType.STUDENT_REACTIVATED, "Learner reactivated for " + student.getFullName(), "Admin", "STUDENT", student.getId());
        return receiptService.issueReceipt("REACTIVATE_STUDENT", "REACTIVATED", "Learner returned to active status.", RuleCode.ENROLL_INACTIVE_STUDENT.format(), "Run enrollment decisions for active courses.", "STUDENT", student.getId());
    }

    public int countActiveStudents() {
        return getActiveStudents().size();
    }

    public int countInactiveStudents() {
        int count = 0;
        for (int i = 0; i < students.size(); i++) {
            if (!students.get(i).isActive()) {
                count++;
            }
        }
        return count;
    }

    public boolean emailExistsForAnotherStudent(String email, int currentStudentId) {
        String normalizedEmail = InputValidator.normalizeEmail(email);
        if (normalizedEmail.length() == 0) {
            return false;
        }
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (student.getId() != currentStudentId && student.getEmail() != null && student.getEmail().equalsIgnoreCase(normalizedEmail)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Person> getStudentsAsPeople() {
        ArrayList<Person> people = new ArrayList<Person>();
        for (int i = 0; i < students.size(); i++) {
            people.add(students.get(i));
        }
        return people;
    }
}
