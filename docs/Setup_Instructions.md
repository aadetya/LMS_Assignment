# Setup Instructions

Use these steps to set up Java, compile LearnTrack, run the console app, and run the verification classes. LearnTrack uses plain Core Java only. No Maven, Gradle, database, JDBC, or external library is required.

---

## 1. JDK version

Recommended JDK:

```text
JDK 17 or newer
```

The cleaned project was prepared for a standard JDK command-line workflow. The code uses common Java features such as classes, packages, enums, `ArrayList`, custom exceptions, `LocalDate`, and `LocalDateTime`.

---

## 2. Check whether Java is installed

Run these commands in a terminal:

```bash
java -version
javac -version
```

What they mean:

- `java -version` checks the Java runtime.
- `javac -version` checks the Java compiler.

If `java` works but `javac` fails, the machine may have only a runtime installed. Install the full JDK and reopen the terminal.

---

## 3. Clean the project before compiling

The submitted repository should not include compiled output or operating-system resource files.

Run this on Linux or macOS:

```bash
find . -name "._*" -delete
rm -rf out
```

Run this on Windows PowerShell:

```powershell
Get-ChildItem -Recurse -Path . -Filter "._*" | Remove-Item -Force
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
```

Why this matters:

- `out/` contains generated `.class` files and should not be treated as source code.
- `._*` files are macOS AppleDouble resource files. They are not valid Java source files, but if they end with `.java`, the compile command may accidentally include them.

---

## 4. Compile on Linux or macOS

From the project root:

```bash
javac -d out $(find src -name "*.java")
```

Explanation:

- `find src -name "*.java"` locates Java source files.
- `javac` compiles them.
- `-d out` places compiled `.class` files into the `out` directory using the same package structure.

---

## 5. Compile on Windows PowerShell

From the project root:

```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
```

Explanation:

- The first command creates a list of source files.
- The second command passes that list to `javac`.

---

## 6. Run the console application

```bash
java -cp out com.airtribe.learntrack.ui.Main
```

Explanation:

- `java` starts the JVM.
- `-cp out` tells Java where compiled classes are located.
- `com.airtribe.learntrack.ui.Main` is the fully qualified class name of the entry point.

---

## 7. Run the smoke test

```bash
java -cp out com.airtribe.learntrack.test.LearnTrackSmokeTest
```

The smoke test checks important service-level behavior without JUnit. It verifies:

- student creation
- duplicate student email rejection
- trainer creation
- course creation with capacity one
- trainer assignment
- accepted enrollment
- waitlisted enrollment
- duplicate open enrollment rejection
- waitlist promotion
- closed enrollment window rejection
- completed enrollment final-state behavior
- polymorphic `Person` behavior
- action log creation
- operation receipt creation
- rule-code formatting

---

## 8. Run the manual scenario runner

```bash
java -cp out com.airtribe.learntrack.test.ManualScenarioRunner
```

The manual scenario runner loads demo data and prints a non-interactive walkthrough. Use it to check the main features without navigating the menu.

---

## 9. Hello World explanation

A minimal Java program looks like this:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
```

Compile it:

```bash
javac HelloWorld.java
```

Run it:

```bash
java HelloWorld
```

LearnTrack follows the same idea, but the code is organized into packages. That is why the run command includes the package path:

```bash
java -cp out com.airtribe.learntrack.ui.Main
```

---

## 10. Troubleshooting

| Problem | Likely cause | Fix |
|---|---|---|
| `javac: command not found` | JDK is missing or PATH is not configured | Install JDK and reopen terminal |
| `Error: Could not find or load main class` | Wrong classpath or project not compiled | Recompile and run with `-cp out` |
| Strange files such as `._Student.java` are compiled | macOS resource files are present | Run `find . -name "._*" -delete` |
| Menu option fails | Invalid input entered | Retry with a listed menu number |
| Cannot enroll student | A business rule blocked it | Check the printed rule code and Rule Catalog |
| Cannot deactivate student/course/trainer | Safe deactivation rule blocked it | Resolve open enrollments or assignments first |
