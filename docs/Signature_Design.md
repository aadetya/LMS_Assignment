# Signature Design

LearnTrack uses learning-operations workflows around students, courses, enrollments, reports, receipts, and journal entries.

---

## 1. Menu names

The menu labels use operational language:

| Area | Menu wording |
|---|---|
| Student operations | Learner Desk |
| Trainer operations | Trainer Desk |
| Course operations | Course Catalog Ops |
| Enrollment operations | Enrollment Desk |
| Reports | Reports & Signals |
| Logs and receipts | Action Journal & Receipts |
| Demonstration | Guided Demo |

Each menu groups related actions.

---

## 2. Operation Receipts

An `OperationReceipt` is created for important successful actions. It contains the operation name, outcome, message, rule summary, next suggested action, reference type, and reference ID.

Example receipt shape:

```text
Receipt #9028
Operation : ENROLL_STUDENT
Outcome   : WAITLISTED
Message   : Course LT-CJF-2001 is full. Student was placed at waitlist position 1.
Rule      : LT-RULE-ENROLL-004: Full course sends learner to waitlist.
Next Step : Monitor waitlist or increase course capacity.
Reference : ENROLLMENT #3006
```

Receipts make each mutation easy to trace.

---

## 3. Rule codes

Important rules are named with stable codes. Examples:

- `LT-RULE-STUDENT-003`
- `LT-RULE-COURSE-005`
- `LT-RULE-ENROLL-004`
- `LT-RULE-DEACTIVATE-002`

The rule-code approach makes validation visible in code, tests, exceptions, receipts, and documentation.

---

## 4. EnrollmentDecision

An enrollment request is treated as a decision, not just object creation.

`EnrollmentDecision` records whether the result was:

- accepted
- waitlisted
- rejected

It also stores the created enrollment, receipt, decision reason, and applied rule code.

Capacity and enrollment windows can change the outcome, so a decision object makes the result explicit.

---

## 5. Capacity and waitlist workflow

Courses have a maximum number of active seats. The enrollment service checks the current active seat count.

If seats are available, the learner receives an active enrollment.

If the course is full but open, the learner receives a waitlisted enrollment.

If an active enrollment is cancelled, the first waitlisted learner is promoted.

The workflow uses `ArrayList`, loops, conditionals, enum status, exceptions, service coordination, and reporting.

---

## 6. Enrollment window

A course can be active while its enrollment window is closed. That gives the admin control over admissions without removing the course from the catalog.

Course states:

- active + open: course accepts enrollments
- active + closed: course exists but blocks new enrollments
- inactive: course is no longer operational

---

## 7. Course Operations Card

The Course Operations Card summarizes a course in one report. It shows:

- code
- name
- level
- trainer
- catalog state
- enrollment window
- max seats
- active seats
- available seats
- waitlist count
- utilization
- operational label
- recommended action

The recommendation is produced using simple rules and `if/else` logic.

---

## 8. Cohort Health Score

The Cohort Health Score starts at 100 and subtracts points for operational risks:

| Signal | Score effect |
|---|---|
| Active course without trainer | -10 |
| Active course over 90 percent full | -10 |
| Waitlist count greater than available seats | -10 |
| Inactive student with open enrollment | -5 |
| Open course with zero active enrollments | -5 |

Labels:

- 85-100: HEALTHY
- 65-84: NEEDS ATTENTION
- below 65: AT RISK

The report explains the signals that affected the score.

---

## 9. Student Learning Trail

The Student Learning Trail prints a student’s learning history. It includes active, waitlisted, completed, and cancelled enrollments. It also shows dates and notes.

The lifecycle model stays visible, and deactivation does not destroy history.

---

## 10. Trainer coverage

Trainer records are used by course assignment and reports. They can be added, updated, assigned to courses, and protected from unsafe deactivation. Reports can show courses with trainers, courses without trainers, and active trainers not assigned to active courses.

Trainer assignment makes the `Trainer extends Person` class useful.

---

## 11. Guided Demo

The guided demo runs a controlled workflow:

1. Identify or create a capacity-one course.
2. Open the enrollment window.
3. Enroll one learner as active.
4. Enroll another learner as waitlisted.
5. Show the Course Operations Card.
6. Cancel the active enrollment.
7. Promote the waitlisted learner.
8. Print receipts and journal entries.
9. Print the Course Operations Card again.
10. Print the Cohort Health Score.

The guided demo gives a quick way to check capacity and promotion behavior.

---

## 12. Core Java boundary

The design stays within Core Java. It uses classes, objects, constructors, overloading, inheritance, overriding, polymorphism, enums, static utilities, `ArrayList`, loops, conditionals, custom exceptions, and console I/O. It does not use frameworks, databases, external libraries, streams, records, or GUI tools.
