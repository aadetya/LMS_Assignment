# Design Decisions Matrix

Key implementation choices and the Java concept behind each one.

| Decision | Alternative | Why LearnTrack uses this decision | Java concept shown | Why it matters |
|---|---|---|---|---|
| Use `ArrayList` for storage | Fixed arrays | Records grow during a session, and `ArrayList` avoids manual resizing | Collections, loops, object storage | Users can keep adding records without array capacity code |
| Keep data in memory | Database or file storage | The assignment focuses on Core Java fundamentals | Objects, references, services | Project compiles with only `javac` |
| Use packages | One large folder | Separates entities, services, UI, exceptions, utilities, docs, and tests | Packages and access modifiers | Code stays easy to navigate |
| Keep `Main` small | Put all menu logic in `Main` | Prevents one oversized class and keeps responsibilities clear | Method calls and composition | Entry point is readable |
| Service layer owns rules | UI checks every rule | Business rules remain testable and reusable | Classes, methods, exceptions | Smoke test can validate logic without menu input |
| Add policy classes | Scatter rule checks everywhere | Enrollment and deactivation rules are easier to find | Focused classes, conditionals | Rule logic is easy to inspect directly |
| Use safe deactivation | Physical deletion | Preserves history and prevents broken records | Boolean state, validation | Learning trails and reports remain meaningful |
| Separate active and enrollment window | One course state | A course may exist but stop accepting new students | Encapsulation and business state | Closed-window behavior is testable |
| Use enum statuses | Raw strings | Avoids typo-prone status values | Enums and switch methods | Status logic is clearer |
| Use rule codes | Plain messages only | Stable codes improve debugging and documentation | Enum methods and exceptions | Errors can be matched to docs |
| Use Operation Receipts | Only print success messages | Receipts show outcome, reason, and next step | Object creation and `ArrayList` storage | Operations leave visible proof |
| Use Action Journal | No mutation history | Internal history helps trace system behavior | `LocalDateTime`, objects, lists | Chronological changes are visible |
| Use EnrollmentDecision | Return only `Enrollment` | Enrollment can be accepted, waitlisted, or blocked | Object composition | Business outcome is explicit |
| Use course capacity | Always enroll | Capacity creates realistic enrollment logic | Counters, loops, if/else | Adds meaningful application behavior |
| Use waitlist promotion | Manual promotion only | Cancelling an active enrollment should fill the seat from waitlist | Loops and status transitions | Demonstrates non-trivial logic |
| Use Trainer as operational entity | Trainer class unused | Trainer can be assigned and reported | Inheritance and service coordination | Inheritance is meaningful |
| Use People Directory | Separate student/trainer print only | Students and trainers can be handled through `Person` | Polymorphism and overriding | Runtime dispatch is visible |
| Use Cohort Health Score | Only list reports | Score summarizes operating risks | Arithmetic and conditionals | Report output includes a single health label |
| Use Guided Demo | Manual discovery only | Shows the capacity and waitlist workflow quickly | Service orchestration | Waitlist behavior can be checked from one menu option |
| Use smoke test with `main` | No test class | Verifies service logic without JUnit | Main method, assertions by helper methods | Quick terminal validation |
