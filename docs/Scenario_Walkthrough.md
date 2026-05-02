# Scenario Walkthrough

A realistic LearnTrack session from the perspective of an admin managing a learning cohort.

---

## 1. Start the application

Compile and run:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.airtribe.learntrack.ui.Main
```

The main menu appears:

```text
==================================================================================================================
                                                    Main Menu
==================================================================================================================
  [ 1] Learner Desk
  [ 2] Trainer Desk
  [ 3] Course Catalog Ops
  [ 4] Enrollment Desk
  [ 5] Reports & Signals
  [ 6] Action Journal & Receipts
  [ 7] Load Demo Data
  [ 8] Guided Demo
  [ 0] Exit
  > Choose option:
```

---

## 2. Load demo data

The admin selects **Load Demo Data**. The seed loader creates sample students, trainers, courses, enrollments, receipts, and journal entries.

The data creates a working system immediately, including courses with capacity and enrollment-window behavior.

---

## 3. Check the dashboard

The admin opens:

```text
Reports & Signals → Learning Pulse Dashboard
```

The dashboard prints counts for students, trainers, courses, enrollments, receipts, and journal entries. This confirms that multiple services are connected.

---

## 4. Check a course

The admin opens a Course Operations Card.

The card shows:

- course code
- course name
- trainer assignment
- catalog state
- enrollment window
- max seats
- active seats
- available seats
- waitlist count
- utilization percentage
- operational label
- recommended action

The card helps the admin see whether a course is empty, filling, full, or carrying a waitlist.

---

## 5. Run the guided enrollment workflow

The admin selects **Guided Demo**.

The demo creates or identifies a capacity-one course and opens enrollment. Then it enrolls the first learner:

```text
Enrollment Decision: ACCEPTED
Status: ACTIVE
```

The demo then enrolls a second learner into the same full course:

```text
Enrollment Decision: WAITLISTED
Status: WAITLISTED
Rule: LT-RULE-ENROLL-004
```

Enrollment is not a blind insert. The service checks seat capacity and returns a decision.

---

## 6. Cancel active enrollment and promote waitlist

The admin cancels the active enrollment. The cancelled enrollment becomes final.

The service then checks the waitlist. The oldest waitlisted enrollment for the course is promoted to active:

```text
WAITLISTED → ACTIVE
```

Separate receipts are created for the cancellation and promotion.

---

## 7. Inspect receipts

The admin opens **Action Journal & Receipts** and views receipts.

Receipts show:

- operation name
- outcome
- message
- rule summary
- next suggested action
- reference type
- reference ID

Receipts make system mutations easy to audit from the console.

---

## 8. Inspect the action journal

The admin opens the action journal.

The journal shows internal chronological events such as:

1. student created
2. trainer created
3. course created
4. trainer assigned
5. enrollment accepted
6. enrollment waitlisted
7. enrollment cancelled
8. waitlisted learner promoted

The journal is in-memory only, but it demonstrates auditability without a database.

---

## 9. Check cohort health

The admin opens the Cohort Health Score.

The score starts at 100 and subtracts points for operational risks:

- active course without trainer
- course over 90 percent full
- waitlist count greater than available seats
- inactive student with open enrollment
- open course with no active enrollments

The report prints the final score, a label, and the signals that affected the score.

---

## 10. View a Student Learning Trail

The admin opens a Student Learning Trail.

The trail prints:

- student profile
- active enrollments
- waitlisted enrollments
- completed enrollments
- cancelled enrollments
- enrollment dates
- last status-change dates
- notes

The report shows why LearnTrack keeps history instead of deleting records.

---

## 11. Run terminal verification

Run:

```bash
java -cp out com.airtribe.learntrack.test.LearnTrackSmokeTest
```

The smoke test verifies the core behavior without requiring menu input.

Then run:

```bash
java -cp out com.airtribe.learntrack.test.ManualScenarioRunner
```

This prints a non-interactive demonstration of the main workflow.
