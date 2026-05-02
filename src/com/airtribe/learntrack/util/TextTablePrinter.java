package com.airtribe.learntrack.util;

import com.airtribe.learntrack.entity.ActionLogEntry;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Person;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Trainer;
import java.util.ArrayList;

public final class TextTablePrinter {
    private TextTablePrinter() {
    }

    public static void printStudents(ArrayList<Student> students) {
        if (students == null || students.size() == 0) {
            ConsolePrinter.printEmpty("learners");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            rows.add(new String[] {
                    String.valueOf(student.getId()),
                    student.getFullName(),
                    student.getEmail(),
                    student.getBatch(),
                    student.isActive() ? "Active" : "Inactive"
            });
        }
        printTable(new String[] {"ID", "Name", "Email", "Batch", "State"}, new int[] {6, 24, 30, 20, 10}, rows);
    }

    public static void printTrainers(ArrayList<Trainer> trainers) {
        if (trainers == null || trainers.size() == 0) {
            ConsolePrinter.printEmpty("trainers");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < trainers.size(); i++) {
            Trainer trainer = trainers.get(i);
            rows.add(new String[] {
                    String.valueOf(trainer.getId()),
                    trainer.getFullName(),
                    trainer.getEmail(),
                    trainer.getExpertise(),
                    trainer.isActive() ? "Active" : "Inactive"
            });
        }
        printTable(new String[] {"ID", "Name", "Email", "Expertise", "State"}, new int[] {6, 24, 30, 22, 10}, rows);
    }

    public static void printCourses(ArrayList<Course> courses) {
        if (courses == null || courses.size() == 0) {
            ConsolePrinter.printEmpty("courses");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            String level = course.getLevel() == null ? "" : course.getLevel().getDisplayText();
            rows.add(new String[] {
                    String.valueOf(course.getId()),
                    course.getCourseCode(),
                    course.getCourseName(),
                    level,
                    course.getOperationalStatus(),
                    String.valueOf(course.getMaxSeats())
            });
        }
        printTable(new String[] {"ID", "Code", "Course", "Level", "Ops Status", "Seats"}, new int[] {6, 14, 28, 12, 26, 7}, rows);
    }

    public static void printEnrollments(ArrayList<Enrollment> enrollments) {
        if (enrollments == null || enrollments.size() == 0) {
            ConsolePrinter.printEmpty("enrollments");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            rows.add(new String[] {
                    String.valueOf(enrollment.getId()),
                    String.valueOf(enrollment.getStudentId()),
                    String.valueOf(enrollment.getCourseId()),
                    enrollment.getStatus().getDisplayText(),
                    String.valueOf(enrollment.getLastStatusChangedOn()),
                    enrollment.getStatusNote()
            });
        }
        printTable(new String[] {"ID", "Student", "Course", "Status", "Changed", "Note"}, new int[] {6, 9, 9, 12, 12, 36}, rows);
    }

    public static void printPeople(ArrayList<Person> people) {
        if (people == null || people.size() == 0) {
            ConsolePrinter.printEmpty("people");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            rows.add(new String[] {String.valueOf(person.getId()), person.getDisplayName()});
        }
        printTable(new String[] {"ID", "Display Name"}, new int[] {6, 88}, rows);
    }

    public static void printActionLog(ArrayList<ActionLogEntry> entries) {
        if (entries == null || entries.size() == 0) {
            ConsolePrinter.printEmpty("action journal entries");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < entries.size(); i++) {
            ActionLogEntry entry = entries.get(i);
            String action = entry.getActionType() == null ? "" : entry.getActionType().getDisplayText();
            rows.add(new String[] {
                    String.valueOf(entry.getId()),
                    action,
                    entry.getSummary(),
                    entry.getActor(),
                    entry.getReferenceType() + " #" + entry.getReferenceId()
            });
        }
        printTable(new String[] {"ID", "Action", "Summary", "Actor", "Reference"}, new int[] {6, 24, 42, 10, 16}, rows);
    }

    public static void printReceipts(ArrayList<OperationReceipt> receipts) {
        if (receipts == null || receipts.size() == 0) {
            ConsolePrinter.printEmpty("operation receipts");
            return;
        }
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < receipts.size(); i++) {
            OperationReceipt receipt = receipts.get(i);
            rows.add(new String[] {
                    String.valueOf(receipt.getReceiptId()),
                    receipt.getOperationName(),
                    receipt.getOutcome(),
                    receipt.getReferenceType(),
                    String.valueOf(receipt.getReferenceId())
            });
        }
        printTable(new String[] {"Receipt", "Operation", "Outcome", "Reference", "ID"}, new int[] {8, 32, 12, 14, 8}, rows);
    }

    public static void printTable(String[] headers, int[] widths, ArrayList<String[]> rows) {
        printBorder(widths);
        printRow(headers, widths);
        printBorder(widths);
        for (int i = 0; i < rows.size(); i++) {
            printWrappedRow(rows.get(i), widths);
        }
        printBorder(widths);
    }

    private static void printWrappedRow(String[] values, int[] widths) {
        ArrayList<String[]> wrappedCells = new ArrayList<String[]>();
        int rowHeight = 1;
        for (int i = 0; i < widths.length; i++) {
            String value = "";
            if (values != null && i < values.length && values[i] != null) {
                value = values[i].replace('|', '/');
            }
            String[] wrapped = wrap(value, widths[i]);
            wrappedCells.add(wrapped);
            if (wrapped.length > rowHeight) {
                rowHeight = wrapped.length;
            }
        }

        for (int line = 0; line < rowHeight; line++) {
            String[] displayValues = new String[widths.length];
            for (int column = 0; column < widths.length; column++) {
                String[] wrapped = wrappedCells.get(column);
                displayValues[column] = line < wrapped.length ? wrapped[line] : "";
            }
            printRow(displayValues, widths);
        }
    }

    private static String[] wrap(String value, int width) {
        ArrayList<String> lines = new ArrayList<String>();
        String text = value == null ? "" : value.trim();
        if (text.length() == 0) {
            lines.add("");
            return lines.toArray(new String[lines.size()]);
        }

        StringBuilder current = new StringBuilder();
        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            while (word.length() > width) {
                if (current.length() > 0) {
                    lines.add(current.toString());
                    current = new StringBuilder();
                }
                lines.add(word.substring(0, width));
                word = word.substring(width);
            }
            if (current.length() == 0) {
                current.append(word);
            } else if (current.length() + 1 + word.length() <= width) {
                current.append(" ").append(word);
            } else {
                lines.add(current.toString());
                current = new StringBuilder(word);
            }
        }
        if (current.length() > 0) {
            lines.add(current.toString());
        }
        return lines.toArray(new String[lines.size()]);
    }

    private static void printRow(String[] values, int[] widths) {
        StringBuilder builder = new StringBuilder();
        builder.append("|");
        for (int i = 0; i < widths.length; i++) {
            String value = "";
            if (values != null && i < values.length && values[i] != null) {
                value = values[i];
            }
            builder.append(" ").append(padRight(value, widths[i])).append(" |");
        }
        System.out.println(builder.toString());
    }

    private static void printBorder(int[] widths) {
        StringBuilder builder = new StringBuilder();
        builder.append("+");
        for (int i = 0; i < widths.length; i++) {
            builder.append(repeat("-", widths[i] + 2)).append("+");
        }
        System.out.println(builder.toString());
    }

    private static String padRight(String value, int width) {
        String safeValue = value == null ? "" : value;
        if (safeValue.length() > width) {
            safeValue = safeValue.substring(0, width);
        }
        StringBuilder builder = new StringBuilder(safeValue);
        while (builder.length() < width) {
            builder.append(" ");
        }
        return builder.toString();
    }

    private static String repeat(String value, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(value);
        }
        return builder.toString();
    }
}
