# Design Notes

Main design decisions in LearnTrack, connected to the Core Java concepts from the assignment.

---

## 1. Why `ArrayList` is used instead of arrays

The number of students, trainers, courses, enrollments, receipts, and action log entries changes while the app is running. A fixed array would require manual resizing and manual copying when capacity is reached.

`ArrayList` is a better fit for this assignment because it is still a Core Java collection and supports dynamic in-memory storage. The services use `ArrayList` to add objects, search objects, return filtered lists, and count records.

Examples:

- `StudentService` stores `ArrayList<Student>`.
- `TrainerService` stores `ArrayList<Trainer>`.
- `CourseService` stores `ArrayList<Course>`.
- `EnrollmentService` stores `ArrayList<Enrollment>`.
- `ActionLogService` stores `ArrayList<ActionLogEntry>`.
- `ReceiptService` stores `ArrayList<OperationReceipt>`.

The code uses normal `for` loops to keep the logic beginner-friendly.

---

## 2. Why data is in memory

LearnTrack does not use a database, file persistence, or JDBC because the assignment focuses on Core Java fundamentals. In-memory services make it easier to demonstrate object creation, package structure, service design, menu flow, exception handling, and `ArrayList` storage.

The limitation is that data disappears when the program exits. That is acceptable here because permanent storage is outside the required scope.

---

## 3. Separation of concerns

The code is split by responsibility.

| Layer | Responsibility | Examples |
|---|---|---|
| Entity | Represents data | `Student`, `Course`, `Enrollment` |
| Service | Stores objects and applies business logic | `StudentService`, `CourseService`, `EnrollmentService` |
| Policy | Holds reusable business rules | `EnrollmentPolicy`, `DeactivationPolicy` |
| UI | Reads input and prints menus | `StudentMenu`, `CourseMenu`, `LearnTrackConsole` |
| Utility | Shared helper methods | `InputValidator`, `IdGenerator`, `ConsolePrinter` |
| Exception | Custom error types | `EntityNotFoundException`, `BusinessRuleViolationException` |

That keeps `Main.java` small. `Main` creates the services and starts the console; it does not contain the full application logic.

---

## 4. Encapsulation

The entity classes use private fields and public getters/setters, so each class controls how its data is accessed.

Examples:

- `Student` has private fields for batch, active status, goal, and join date.
- `Course` has private fields for course code, active state, enrollment window, level, and trainer ID.
- `Enrollment` has private fields for status, enrollment date, status-change date, and note.

Encapsulation also makes service logic clearer because services control when important changes occur.

---

## 5. Constructors and constructor overloading

Constructors are used to create objects in valid initial states.

Constructor overloading appears in entities such as:

- `Student`
- `Course`
- `Enrollment`

This allows the code to support both complete object creation and simpler object creation. For example, a course can be created with all details or with a shorter constructor that uses default values.

---

## 6. Method overloading

Method overloading appears in service methods. For example:

```java
addStudent(String firstName, String lastName, String email, String batch, String learningGoal)
addStudent(String firstName, String lastName, String batch)
```

```java
addCourse(String courseName, String description, int durationInWeeks, int maxSeats, CourseLevel level)
addCourse(String courseName, int durationInWeeks)
```

The overloaded versions provide a simple path and a detailed path while keeping method names meaningful.

---

## 7. Static members

`IdGenerator` uses static counters and static methods because ID generation belongs to the application session, not to one specific object instance.

Examples:

- `getNextStudentId()`
- `getNextTrainerId()`
- `getNextCourseId()`
- `getNextEnrollmentId()`
- `getNextActionLogId()`
- `getNextReceiptId()`

Other utility classes also use static methods because they do not need object state:

- `InputValidator`
- `ConsolePrinter`
- `TextTablePrinter`
- `CourseCodeGenerator`
- `RuleExplainer`

---

## 8. Inheritance

`Person` is the base class for people in the system. It contains shared fields:

- `id`
- `firstName`
- `lastName`
- `email`

`Student` and `Trainer` extend `Person`.

A simple `is-a` relationship:

- A student is a person.
- A trainer is a person.

It avoids repeating common fields in both classes.

---

## 9. Method overriding

`Person` has a general `getDisplayName()` method. `Student` and `Trainer` override it to show role-specific information.

Example student display:

```text
Student #1001 - Asha Nair | Batch: Java Foundations | Active
```

Example trainer display:

```text
Trainer #501 - Rohit Mehta | Expertise: Core Java | Active
```

Overriding allows the same method name to produce behavior based on the actual object type.

---

## 10. Polymorphism

The People Directory report demonstrates polymorphism. It combines students and trainers into an `ArrayList<Person>` and then prints each object by calling `getDisplayName()`.

At runtime, Java decides whether to call the student version or trainer version of the method. This is a practical use of polymorphism rather than a token example.

---

## 11. Why course active state and enrollment window are separate

A course has two different states:

- `active`
- `enrollmentOpen`

`active` means the course exists in the catalog. `enrollmentOpen` means the course currently accepts new enrollments.

This distinction matters. A course can remain active while admissions are closed. That is more realistic than using only one flag.

---

## 12. Why waitlist is an enrollment status

Waitlisting is modeled as `EnrollmentStatus.WAITLISTED`. It is not stored as an unrelated list.

Modeling waitlist as a status keeps the learner-course relationship in one entity. The Student Learning Trail can show active, waitlisted, completed, and cancelled enrollments from the same data source.

---

## 13. Enrollment lifecycle

Enrollments use these statuses:

- `ACTIVE`
- `WAITLISTED`
- `COMPLETED`
- `CANCELLED`

Allowed transitions:

- `ACTIVE` can become `COMPLETED`.
- `ACTIVE` can become `CANCELLED`.
- `WAITLISTED` can become `ACTIVE`.
- `WAITLISTED` can become `CANCELLED`.

Final states:

- `COMPLETED`
- `CANCELLED`

Final states cannot be changed again. This rule is enforced through `EnrollmentPolicy`.

---

## 14. Safe deactivation

LearnTrack avoids physical deletion. It uses safe deactivation rules:

- A student with open enrollments cannot be deactivated.
- A course with active enrollments cannot be deactivated.
- A trainer assigned to an active course cannot be deactivated.

Safe deactivation protects historical data and prevents broken relationships.

---

## 15. Rule codes

`RuleCode` gives important rules stable identifiers.

Examples:

```text
LT-RULE-ENROLL-003: Duplicate open enrollment is not allowed.
LT-RULE-COURSE-005: Closed enrollment window blocks new enrollment.
LT-RULE-DEACTIVATE-002: Course with active enrollments cannot be deactivated.
```

Rule codes make error messages easier to understand and verify.

---

## 16. Operation receipts

Operation receipts are user-facing confirmations. They record:

- receipt ID
- timestamp
- operation name
- outcome
- main message
- rule summary
- next suggested action
- reference type
- reference ID

Operation receipts give important actions a traceable result rather than only printing a short success line.

---

## 17. Action journal

The action journal records internal mutation history. It is separate from receipts.

Receipts are user-facing confirmations. Journal entries are internal system history. Together, they make the application easier to inspect without needing a database or log file.

---

## 18. Policy classes

`EnrollmentPolicy` handles enrollment rules and status transitions. `DeactivationPolicy` handles safe deactivation rules.

Keeping rules in policy classes leaves menu classes focused on input and output. Services and policies decide what is allowed.

---

## 19. Exception handling

LearnTrack uses custom checked exceptions:

- `EntityNotFoundException`
- `InvalidInputException`
- `DuplicateEntityException`
- `BusinessRuleViolationException`
- `InvalidEnrollmentStateException`

The UI catches these exceptions and prints readable messages. This prevents the console from crashing when the user enters invalid data or tries an operation blocked by a rule.
