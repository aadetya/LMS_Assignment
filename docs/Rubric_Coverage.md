# Rubric Coverage

Rubric map for LearnTrack, with the file or class that handles each requirement.

---

## A. Environment Setup and JVM Understanding — 10 marks

| Requirement | Evidence |
|---|---|
| JDK setup explained | `docs/Setup_Instructions.md` |
| Compile and run commands | README and `docs/Setup_Instructions.md` |
| Hello World explanation | `docs/Setup_Instructions.md` |
| JDK, JRE, JVM explained | `docs/JVM_Basics.md` |
| Bytecode explained | `docs/JVM_Basics.md` |
| Write once, run anywhere explained | `docs/JVM_Basics.md` |

Verification command:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.airtribe.learntrack.ui.Main
```

---

## B. Package Structure and Basics — 10 marks

| Requirement | Evidence |
|---|---|
| Base package | `com.airtribe.learntrack` |
| Entity package | `com.airtribe.learntrack.entity` |
| Service package | `com.airtribe.learntrack.service` |
| UI package | `com.airtribe.learntrack.ui` |
| Exception package | `com.airtribe.learntrack.exception` |
| Utility package | `com.airtribe.learntrack.util` |
| Documentation folder | `docs/` |
| Static variables/methods | `IdGenerator`, `InputValidator`, `ConsolePrinter`, `TextTablePrinter`, `RuleExplainer` |
| Access modifiers | private fields, public service methods, package organization |

Additional package depth:

- `entity.enums`
- `service.policy`
- `test`

---

## C. Core OOP Implementation — 40 marks

### C1. Entities and encapsulation — 15 marks

| Required entity | Required fields | Evidence |
|---|---|---|
| `Student` | `id`, `firstName`, `lastName`, `email`, `batch`, `active` | `Student.java`, with inherited person fields and student-specific fields |
| `Course` | `id`, `courseName`, `description`, `durationInWeeks`, `active` | `Course.java` |
| `Enrollment` | `id`, `studentId`, `courseId`, `enrollmentDate`, `status` | `Enrollment.java` |

Additional entities:

- `Person`
- `Trainer`
- `EnrollmentDecision`
- `OperationReceipt`
- `ActionLogEntry`

Encapsulation evidence:

- fields are private
- getters and setters are provided
- display/helper methods are included where useful

### C2. Constructors, overloading, and methods

| Concept | Evidence |
|---|---|
| Default constructors | Core entity classes |
| Parameterized constructors | Core entity classes |
| Constructor overloading | `Student`, `Course`, `Enrollment` |
| Method overloading | `StudentService.addStudent(...)`, `CourseService.addCourse(...)` |
| Meaningful methods | `findStudentById`, `openEnrollmentWindow`, `promoteNextWaitlistedLearner`, `printCohortHealthScore` |

### C3. Inheritance and polymorphism — 10 marks

| Concept | Evidence |
|---|---|
| Base class | `Person` |
| Inheritance | `Student extends Person`, `Trainer extends Person` |
| Use of `super` | Constructors in `Student` and `Trainer` |
| Method overriding | `getDisplayName()` in `Student` and `Trainer` |
| Polymorphism | People Directory combines students and trainers as `ArrayList<Person>` |

### C4. Static and utility classes — 15 marks

| Concept | Evidence |
|---|---|
| Static ID counters | `IdGenerator` |
| Static validation helpers | `InputValidator` |
| Static print helpers | `ConsolePrinter`, `TextTablePrinter` |
| Static rule explanation | `RuleExplainer` |
| Static course-code generation | `CourseCodeGenerator` |

---

## D. Application Logic and Menu-Driven Console UI — 25 marks

| Required feature | Evidence |
|---|---|
| Add student | Learner Desk and `StudentService.addStudent()` |
| View all students | Learner Desk and `TextTablePrinter.printStudents()` |
| Search student by ID | Learner Desk and `StudentService.findStudentById()` |
| Deactivate student | Learner Desk and `StudentService.deactivateStudent()` |
| Add course | Course Catalog Ops and `CourseService.addCourse()` |
| View all courses | Course Catalog Ops |
| Activate/deactivate course | `CourseService.reactivateCourse()` and `CourseService.deactivateCourse()` |
| Enroll student in course | Enrollment Desk and `EnrollmentService.enrollStudent()` |
| View enrollments for student | Enrollment Desk and Student Learning Trail |
| Mark enrollment completed/cancelled | Enrollment Desk and `EnrollmentService` |
| ArrayList storage | all services |
| In-memory only | no file database or external persistence |
| Loops and conditionals | services, menus, policies, reports |
| Main focused on starting app | `Main` only creates services and starts `LearnTrackConsole` |

Additional application logic:

- capacity-based enrollment
- waitlisted enrollments
- automatic waitlist promotion
- enrollment window control
- trainer assignment
- safe deactivation rules
- guided demo
- action journal
- operation receipts
- operational reports

---

## E. Basic Exception Handling — 10 marks

| Requirement | Evidence |
|---|---|
| Custom exception for missing records | `EntityNotFoundException` |
| Invalid input exception | `InvalidInputException` |
| Duplicate exception | `DuplicateEntityException` |
| Business rule exception | `BusinessRuleViolationException` |
| Enrollment state exception | `InvalidEnrollmentStateException` |
| try-catch in menu operations | UI menu classes |
| Clean messages | `ConsolePrinter.printError()` and rule-coded messages |

Exception handling is tied to real behavior:

- duplicate student email
- duplicate trainer email
- duplicate active course name
- missing student or course ID
- inactive course enrollment
- closed enrollment window
- duplicate open enrollment
- final enrollment state update
- unsafe deactivation

---

## F. Documentation and Clean Code — 5 marks

| Requirement | Evidence |
|---|---|
| README | `README.md` |
| Compile/run instructions | README and `Setup_Instructions.md` |
| Design notes | `Design_Notes.md` |
| Class diagram | Architecture diagram image in README |
| Clean package separation | source structure |
| Meaningful names | service, menu, report, and policy methods use descriptive names |

Additional documentation:

- `Signature_Design.md`
- `Design_Decisions_Matrix.md`
- `Rule_Catalog.md`
- `Manual_Test_Cases.md`
- `Demo_Guide.md`
- `Scenario_Walkthrough.md`

---

## Coverage summary

The project includes the required setup docs, package structure, OOP implementation, menu UI, service logic, custom exceptions, diagrams, screenshots, and terminal verification classes.
