package com.airtribe.learntrack.util;

import java.util.ArrayList;

public final class ConsolePrinter {
    private static final int LINE_WIDTH = 114;
    private static final int KEY_WIDTH = 26;

    private ConsolePrinter() {
    }

    public static void printAppBanner() {
        printSeparator();
        printCentered("LearnTrack");
        printCentered("Cohort Learning Operations Console");
        printSeparator();
    }

    public static void printHeader(String title) {
        System.out.println();
        printSeparator();
        printCentered(title);
        printSeparator();
    }

    public static void printSubHeader(String title) {
        System.out.println();
        String label = " " + title + " ";
        int remaining = LINE_WIDTH - label.length();
        if (remaining < 0) {
            System.out.println(label.trim());
            return;
        }
        int left = remaining / 2;
        int right = remaining - left;
        System.out.println(repeat("-", left) + label + repeat("-", right));
    }

    public static void printSuccess(String message) {
        printStatus("SUCCESS", message);
    }

    public static void printWarning(String message) {
        printStatus("WARNING", message);
    }

    public static void printError(String message) {
        printStatus("ERROR", message);
    }

    public static void printInfo(String message) {
        printStatus("INFO", message);
    }

    public static void printEmpty(String label) {
        printStatus("EMPTY", "No " + label + " found.");
    }

    public static void printSeparator() {
        System.out.println(repeat("=", LINE_WIDTH));
    }

    public static void printKeyValue(String key, String value) {
        printWrappedKeyValue(key, value, null);
    }

    public static void printMenuOption(String key, String label) {
        System.out.printf("  [%2s] %s%n", key, label);
    }

    public static String formatKeyValueBlock(String title, String[][] rows) {
        StringBuilder builder = new StringBuilder();
        builder.append(title).append("\n");
        builder.append(repeat("-", LINE_WIDTH)).append("\n");
        for (int i = 0; i < rows.length; i++) {
            String key = rows[i][0] == null ? "" : rows[i][0];
            String value = rows[i][1] == null ? "" : rows[i][1];
            printWrappedKeyValue(key, value, builder);
            if (i < rows.length - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private static void printStatus(String label, String message) {
        System.out.printf("  %-9s %s%n", "[" + label + "]", message);
    }

    private static void printWrappedKeyValue(String key, String value, StringBuilder target) {
        String safeKey = key == null ? "" : key;
        String safeValue = value == null ? "" : value;
        String prefix = String.format("  %-" + KEY_WIDTH + "s : ", safeKey);
        String continuationPrefix = repeat(" ", prefix.length());
        String[] lines = wrapText(safeValue, LINE_WIDTH - prefix.length());
        for (int i = 0; i < lines.length; i++) {
            String linePrefix = i == 0 ? prefix : continuationPrefix;
            if (target == null) {
                System.out.println(linePrefix + lines[i]);
            } else {
                target.append(linePrefix).append(lines[i]);
                if (i < lines.length - 1) {
                    target.append("\n");
                }
            }
        }
    }

    private static String[] wrapText(String value, int width) {
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

    private static void printCentered(String text) {
        String safeText = text == null ? "" : text;
        if (safeText.length() >= LINE_WIDTH) {
            System.out.println(safeText);
            return;
        }
        int padding = (LINE_WIDTH - safeText.length()) / 2;
        System.out.println(repeat(" ", padding) + safeText);
    }

    private static String repeat(String value, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(value);
        }
        return builder.toString();
    }
}
