# Manual Test Cases

These tests can be performed through the console application. They cover the required assignment behavior plus capacity, waitlist, reports, receipts, and journal entries.

---

## Before testing

Compile and run:

```bash
find . -name "._*" -delete
rm -rf out
javac -d out $(find src -name "*.java")
java -cp out com.airtribe.learntrack.ui.Main
```

For most tests, first select:

```text
7. Load Demo Data
```

---

## Manual tests

| Test ID | Scenario | Steps | Expected result | Requirement covered |
|---|---|---|---|---|
| MT-01 | Add valid learner | Learner Desk → Add student → enter valid details | Student is created and a receipt appears | Entity creation, service method, receipt |
| MT-02 | Reject blank learner name | Add student with blank first name | Error with `LT-RULE-STUDENT-001` | Input validation, custom exception |
| MT-03 | Reject invalid email | Add student with email `abc` | Error with `LT-RULE-STUDENT-002` | Email validation |
| MT-04 | Reject duplicate student email | Add two students with same email | Second add fails with `LT-RULE-STUDENT-003` | Duplicate prevention |
| MT-05 | Search student by ID | Learner Desk → Search by ID | Correct student details are printed | Loops, service search |
| MT-06 | Search student by email | Learner Desk → Search by email | Correct student details are printed | String normalization and search |
| MT-07 | Search students by name keyword | Learner Desk → Search by name | Matching students are printed | Filtered `ArrayList` |
| MT-08 | Update student profile | Learner Desk → Update profile | Updated values are shown and receipt/journal entry is created | Encapsulation and update method |
| MT-09 | Block unsafe student deactivation | Try deactivating a student with active/waitlisted enrollment | Error with `LT-RULE-DEACTIVATE-001` | Safe deactivation |
| MT-10 | Reactivate student | Deactivate eligible student, then reactivate | Student becomes active again | Lifecycle instead of deletion |
| MT-11 | Add trainer | Trainer Desk → Add trainer | Trainer appears in trainer list | Inheritance via `Trainer extends Person` |
| MT-12 | Search trainer | Trainer Desk → Search trainer by name or ID | Trainer details are printed | Service search |
| MT-13 | Assign trainer to course | Course Catalog Ops → Assign trainer | Course card shows trainer | Trainer-course relationship |
| MT-14 | Block trainer deactivation | Assign trainer to active course, then deactivate trainer | Error with `LT-RULE-DEACTIVATE-003` | Safe trainer deactivation |
| MT-15 | Add course | Course Catalog Ops → Add course | Course is created with generated code | Static utility and constructor |
| MT-16 | Reject duplicate active course name | Add same active course name again | Error with `LT-RULE-COURSE-003` | Duplicate prevention |
| MT-17 | Open enrollment window | Course Catalog Ops → Open enrollment window | Course becomes open for enrollment | Course lifecycle |
| MT-18 | Close enrollment window | Course Catalog Ops → Close enrollment window | Course remains active but blocks new enrollment | Separate active/window state |
| MT-19 | Reject enrollment into closed course | Close window, then Enrollment Desk → Enroll student | Error with `LT-RULE-COURSE-005` | Enrollment policy |
| MT-20 | Accept enrollment with available seat | Enroll student into open course with free seat | Decision is ACCEPTED and status is ACTIVE | EnrollmentDecision |
| MT-21 | Capacity creates waitlist | Use capacity-one course and enroll two students | First is ACTIVE, second is WAITLISTED | Capacity and waitlist |
| MT-22 | Reject duplicate open enrollment | Enroll same student into same course again while open | Error with `LT-RULE-ENROLL-003` | Duplicate open enrollment rule |
| MT-23 | Cancel active enrollment and promote waitlist | Cancel active enrollment in a course with waitlist | Oldest waitlisted enrollment becomes ACTIVE | Waitlist promotion |
| MT-24 | Complete active enrollment | Enrollment Desk → Mark completed | Status becomes COMPLETED | Status lifecycle |
| MT-25 | Reject cancelling completed enrollment | Complete enrollment, then cancel it | Error with `LT-RULE-ENROLL-005` | Final-state rule |
| MT-26 | Reject updating completed enrollment again | Complete enrollment, then complete again | Error with `LT-RULE-ENROLL-005` | Final-state lock |
| MT-27 | Block course deactivation with active enrollment | Deactivate course that has active learner | Error with `LT-RULE-DEACTIVATE-002` | Safe deactivation |
| MT-28 | View all students | Learner Desk → View all students | Student table is printed or empty message appears | Menu UI and ArrayList |
| MT-29 | View all courses | Course Catalog Ops → View all courses | Course table is printed or empty message appears | Menu UI and ArrayList |
| MT-30 | View enrollments for student | Enrollment Desk → View enrollments for student | Student's enrollments are printed | Enrollment search |
| MT-31 | Learning Pulse Dashboard | Reports & Signals → Learning Pulse Dashboard | Counts for students, trainers, courses, enrollments, logs, receipts | Reporting |
| MT-32 | Course Operations Card | Reports & Signals → Course Operations Card | Capacity, trainer, state, label, and action are shown | Rich reporting |
| MT-33 | Cohort Health Score | Reports & Signals → Cohort Health Score | Score, label, and risk signals are printed | Conditional report logic |
| MT-34 | Capacity Report | Reports & Signals → Capacity Report | Seat counts and utilization are printed | Arithmetic and loops |
| MT-35 | Waitlist Report | Reports & Signals → Waitlist Report | Waitlisted learners are shown in order | ArrayList order |
| MT-36 | Trainer Coverage Report | Reports & Signals → Trainer Coverage Report | Covered/uncovered courses and free trainers are shown | Trainer use case |
| MT-37 | People Directory | Reports & Signals → People Directory | Students and trainers print through `Person` display | Polymorphism |
| MT-38 | Student Learning Trail | Learner Desk or Reports → Student Learning Trail | Student profile and timeline entries are shown | Lifecycle history |
| MT-39 | View Action Journal | Action Journal & Receipts → View journal | Chronological events are printed | Auditability |
| MT-40 | View Operation Receipts | Action Journal & Receipts → View receipts | Receipt blocks are printed | Receipts and references |
| MT-41 | Guided Demo | Main menu → Guided Demo | Accepted enrollment, waitlisting, cancellation, promotion, reports, receipts | Integrated workflow |
| MT-42 | Invalid menu input | Enter invalid menu number or non-number ID | App prints clean error and continues | Exception handling |
| MT-43 | Smoke test | Run `java -cp out com.airtribe.learntrack.test.LearnTrackSmokeTest` | PASS summary appears | Service-level verification |
| MT-44 | Manual scenario runner | Run `java -cp out com.airtribe.learntrack.test.ManualScenarioRunner` | Reports and demo output print without user input | Non-interactive demo |

---

## Fast demo sequence

Use this order for a quick end-to-end check:

1. Load demo data.
2. Open Learning Pulse Dashboard.
3. Open Course Operations Card for a course.
4. Run Guided Demo.
5. View recent Operation Receipts.
6. View recent Action Journal entries.
7. View Cohort Health Score.
8. Run the smoke test from terminal.

The sequence covers the required fundamentals plus capacity, waitlist, promotion, reports, receipts, journal entries, and rule-based validation.
