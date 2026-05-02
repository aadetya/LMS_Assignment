package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.enums.CourseLevel;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.util.InputValidator;
import java.util.Scanner;

public class ConsoleReader {
    private Scanner scanner;

    public ConsoleReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print("  > " + prompt);
        return scanner.nextLine();
    }

    public String readOptionalLine(String prompt) {
        System.out.print("  > " + prompt);
        return scanner.nextLine();
    }

    public int readPositiveInt(String prompt) throws InvalidInputException {
        String rawValue = readLine(prompt);
        return InputValidator.parsePositiveInt(rawValue, prompt);
    }

    public int readMenuChoice(String prompt) throws InvalidInputException {
        String rawValue = readLine(prompt);
        try {
            int value = Integer.parseInt(rawValue.trim());
            if (value < 0) {
                throw new InvalidInputException(RuleCode.INPUT_MENU_CHOICE_INVALID.format() + " Choice: " + rawValue);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new InvalidInputException(RuleCode.INPUT_MENU_CHOICE_INVALID.format() + " Choice: " + rawValue, exception);
        }
    }

    public CourseLevel readCourseLevel(String prompt) throws InvalidInputException {
        String rawValue = readLine(prompt);
        return InputValidator.parseCourseLevel(rawValue);
    }

    public void pause() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }
}
