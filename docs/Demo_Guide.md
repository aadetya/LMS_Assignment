# Demo Guide

Use this path to check LearnTrack quickly.

---

## 1. Clean and compile

Linux or macOS:

```bash
find . -name "._*" -delete
rm -rf out
javac -d out $(find src -name "*.java")
```

Windows PowerShell:

```powershell
Get-ChildItem -Recurse -Path . -Filter "._*" | Remove-Item -Force
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
```

---

## 2. Run the app

```bash
java -cp out com.airtribe.learntrack.ui.Main
```

Expected main menu:

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

## 3. Load demo data

Choose:

```text
7. Load Demo Data
```

Expected result:

- students are created
- trainers are created
- courses are created
- enrollments are created
- at least one capacity/waitlist situation exists
- action log entry is recorded
- operation receipt is created

Loading seed data twice in the same console session should be blocked.

---

## 4. View Learning Pulse Dashboard

Choose:

```text
5. Reports & Signals
1. Learning Pulse Dashboard
```

Expected result:

The dashboard prints counts for students, trainers, courses, open/closed course windows, active/waitlisted/completed/cancelled enrollments, journal entries, and receipts.

---

## 5. Run Guided Demo

Return to the main menu and choose:

```text
8. Guided Demo
```

The demo should show this workflow:

```text
first learner  -> ACTIVE
second learner -> WAITLISTED
cancel active enrollment
waitlisted learner -> ACTIVE
```

It should also print receipts, journal entries, a Course Operations Card, and the Cohort Health Score.

---

## 6. Inspect operation receipts

Choose:

```text
6. Action Journal & Receipts
```

Then select the receipt view option.

Look for receipts related to:

- student creation
- course creation
- enrollment accepted
- enrollment waitlisted
- enrollment cancelled
- waitlist promotion
- guided demo completion

Each receipt should show operation name, outcome, reference type, reference ID, and next suggested action.

---

## 7. Inspect the Action Journal

In the same Action Journal & Receipts menu, open the action journal.

Look for events such as:

- `STUDENT_CREATED`
- `COURSE_CREATED`
- `TRAINER_ASSIGNED`
- `ENROLLMENT_ACCEPTED`
- `ENROLLMENT_WAITLISTED`
- `ENROLLMENT_CANCELLED`
- `ENROLLMENT_PROMOTED`

The journal proves that service-layer operations leave internal history.

---

## 8. View Course Operations Card

Choose:

```text
5. Reports & Signals
Course Operations Card
```

Enter a course ID. The card should show:

- course code
- course name
- level
- trainer
- active/inactive state
- enrollment window state
- max seats
- active seats
- available seats
- waitlist count
- utilization
- operational label
- recommended action

---

## 9. View Cohort Health Score

Choose:

```text
5. Reports & Signals
Cohort Health Score
```

Expected result:

The report prints a score out of 100, a label, and the rules that affected the score.

---

## 10. Terminal checks

Run the smoke test:

```bash
java -cp out com.airtribe.learntrack.test.LearnTrackSmokeTest
```

Run the manual scenario:

```bash
java -cp out com.airtribe.learntrack.test.ManualScenarioRunner
```

These checks are useful because they verify service behavior without relying on menu input.
