# JVM Basics

LearnTrack is a Core Java project, so the normal Java compile-and-run flow is important. The key pieces are the JDK, JRE, JVM, bytecode, classpath, and the phrase “write once, run anywhere.”

---

## JDK

**JDK** means **Java Development Kit**.

The JDK is used by developers. It includes the Java compiler, runtime tools, and other utilities needed to build Java programs. The most important JDK command for this project is:

```bash
javac
```

`javac` compiles source files such as:

```text
src/com/airtribe/learntrack/entity/Student.java
src/com/airtribe/learntrack/service/EnrollmentService.java
src/com/airtribe/learntrack/ui/Main.java
```

The compile command for LearnTrack is:

```bash
javac -d out $(find src -name "*.java")
```

---

## JRE

**JRE** means **Java Runtime Environment**.

The JRE contains the pieces needed to run compiled Java programs. A person who only runs Java software needs a runtime. A person who writes and compiles Java code needs the JDK.

Modern JDK installations include the runtime components, so installing the JDK is enough for this project.

---

## JVM

**JVM** means **Java Virtual Machine**.

The JVM executes compiled Java bytecode. When this command runs:

```bash
java -cp out com.airtribe.learntrack.ui.Main
```

the JVM loads the compiled class files from `out`, finds the `main` method in `com.airtribe.learntrack.ui.Main`, and starts the console application.

---

## Bytecode

Java source code is written in `.java` files. After compilation, Java produces `.class` files containing bytecode.

Example source file:

```text
src/com/airtribe/learntrack/entity/Course.java
```

Example compiled file:

```text
out/com/airtribe/learntrack/entity/Course.class
```

Bytecode is not normal source code. It is an intermediate format that the JVM understands.

---

## Classpath

The classpath tells Java where compiled classes are stored.

In LearnTrack:

```bash
java -cp out com.airtribe.learntrack.ui.Main
```

- `-cp` means classpath.
- `out` is the compiled output folder.
- `com.airtribe.learntrack.ui.Main` is the fully qualified class name.

---

## Write once, run anywhere

Java is often described as **write once, run anywhere**. The source code is compiled into bytecode, and the bytecode can run on any operating system that has a compatible JVM.

For LearnTrack, the same source files can run on Windows, macOS, or Linux. The terminal commands may differ slightly, but the Java code does not need to change.

---

## How this applies to LearnTrack

LearnTrack flow:

1. Write `.java` files under `src/`.
2. Compile them with `javac`.
3. Store compiled `.class` files under `out/`.
4. Start the JVM with the `java` command.
5. Run the `Main` class.
6. Use the console menus to call service methods.
