# Rule Catalog

LearnTrack uses rule codes to show exactly which rule blocked or shaped an operation. A rule code is a stable identifier for a business rule used in services, policies, exceptions, receipts, and tests.

---

## Rule-code format

```text
LT-RULE-AREA-NUMBER
```

Examples:

```text
LT-RULE-STUDENT-003
LT-RULE-COURSE-005
LT-RULE-ENROLL-004
LT-RULE-DEACTIVATE-002
```

---

## Rule table

| Rule code | Enum name | Meaning | Main location | Typical result | Manual test idea |
|---|---|---|---|---|---|
| `LT-RULE-STUDENT-001` | `STUDENT_NAME_REQUIRED` | Student name fields cannot be blank | `InputValidator`, student creation/update | `InvalidInputException` | Add student with blank first name |
| `LT-RULE-STUDENT-002` | `STUDENT_EMAIL_INVALID` | Email can be blank, but if present must contain `@` and `.` | `InputValidator` | `InvalidInputException` | Add student with `abc` email |
| `LT-RULE-STUDENT-003` | `STUDENT_EMAIL_UNIQUE` | Student email must be unique | `StudentService` | `DuplicateEntityException` | Add two students with the same email |
| `LT-RULE-COURSE-001` | `COURSE_NAME_REQUIRED` | Course name cannot be blank | `InputValidator`, `CourseService` | `InvalidInputException` | Add blank course name |
| `LT-RULE-COURSE-002` | `COURSE_CODE_UNIQUE` | Course code must be unique | `CourseService` | `DuplicateEntityException` | Create a duplicate course-code case |
| `LT-RULE-COURSE-003` | `COURSE_NAME_UNIQUE_ACTIVE` | Active course names should not be duplicated exactly | `CourseService` | `DuplicateEntityException` | Add same active course name twice |
| `LT-RULE-COURSE-004` | `COURSE_INACTIVE` | Inactive course cannot accept enrollment | `EnrollmentPolicy` | `BusinessRuleViolationException` | Deactivate course and attempt enrollment |
| `LT-RULE-COURSE-005` | `COURSE_ENROLLMENT_CLOSED` | Closed enrollment window blocks new enrollment | `EnrollmentPolicy` | `BusinessRuleViolationException` | Close window and enroll student |
| `LT-RULE-COURSE-006` | `COURSE_CAPACITY_POSITIVE` | Course seat capacity must be positive | `InputValidator` | `InvalidInputException` | Add course with zero seats |
| `LT-RULE-TRAINER-001` | `TRAINER_EMAIL_UNIQUE` | Trainer email must be unique | `TrainerService` | `DuplicateEntityException` | Add two trainers with same email |
| `LT-RULE-TRAINER-002` | `TRAINER_INACTIVE_ASSIGNMENT` | Inactive trainer cannot be assigned to a course | `CourseService.assignTrainer()` | `BusinessRuleViolationException` | Deactivate trainer and assign to course |
| `LT-RULE-ENROLL-001` | `ENROLL_STUDENT_REQUIRED` | Student must exist before enrollment | `StudentService.findStudentById()` | `EntityNotFoundException` | Enroll with missing student ID |
| `LT-RULE-ENROLL-002` | `ENROLL_COURSE_REQUIRED` | Course must exist before enrollment | `CourseService.findCourseById()` | `EntityNotFoundException` | Enroll with missing course ID |
| `LT-RULE-ENROLL-003` | `ENROLL_DUPLICATE_OPEN` | Student cannot have two open enrollments in same course | `EnrollmentPolicy` | `DuplicateEntityException` | Enroll same student twice into same course |
| `LT-RULE-ENROLL-004` | `ENROLL_CAPACITY_FULL_WAITLIST` | Full course sends learner to waitlist if enrollment is open | `EnrollmentService` | `EnrollmentDecision.waitlisted()` | Capacity-one course with two learners |
| `LT-RULE-ENROLL-005` | `ENROLL_FINAL_STATE` | Completed/cancelled enrollments are final | `EnrollmentPolicy` | `InvalidEnrollmentStateException` | Complete enrollment, then cancel it |
| `LT-RULE-ENROLL-006` | `ENROLL_INACTIVE_STUDENT` | Inactive student cannot be enrolled | `EnrollmentPolicy` | `BusinessRuleViolationException` | Deactivate student and attempt enrollment |
| `LT-RULE-DEACTIVATE-001` | `DEACTIVATE_STUDENT_HAS_OPEN_ENROLLMENTS` | Student with open enrollments cannot be deactivated | `DeactivationPolicy` | `BusinessRuleViolationException` | Deactivate student with active enrollment |
| `LT-RULE-DEACTIVATE-002` | `DEACTIVATE_COURSE_HAS_ACTIVE_ENROLLMENTS` | Course with active enrollments cannot be deactivated | `DeactivationPolicy` | `BusinessRuleViolationException` | Deactivate course with active enrollment |
| `LT-RULE-DEACTIVATE-003` | `DEACTIVATE_TRAINER_ASSIGNED_TO_ACTIVE_COURSE` | Trainer assigned to an active course cannot be deactivated | `DeactivationPolicy` | `BusinessRuleViolationException` | Assign trainer and deactivate trainer |
| `LT-RULE-INPUT-001` | `INPUT_POSITIVE_NUMBER_REQUIRED` | Positive number is required for IDs and counts | `InputValidator` | `InvalidInputException` | Enter text or negative number for ID |
| `LT-RULE-INPUT-002` | `INPUT_MENU_CHOICE_INVALID` | Menu choice or course level is invalid | UI and validator methods | `InvalidInputException` | Enter invalid menu option |

---

## Why rule codes are useful

A plain message such as `invalid enrollment` is difficult to trace. A rule code such as `LT-RULE-ENROLL-003` points to one specific behavior: duplicate open enrollment is not allowed.

Each rule can be checked in four places:

1. the source code,
2. the exception message,
3. the receipt or console output,
4. this rule catalog.

---

## Example messages

Duplicate open enrollment:

```text
LT-RULE-ENROLL-003: Duplicate open enrollment is not allowed.
```

Full course waitlist:

```text
LT-RULE-ENROLL-004: Full course sends learner to waitlist.
```

Closed enrollment window:

```text
LT-RULE-COURSE-005: Closed enrollment window blocks new enrollment.
```

Unsafe course deactivation:

```text
LT-RULE-DEACTIVATE-002: Course with active enrollments cannot be deactivated.
```
