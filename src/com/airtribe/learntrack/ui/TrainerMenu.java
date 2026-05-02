package com.airtribe.learntrack.ui;

import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.ReceiptService;
import com.airtribe.learntrack.service.TrainerService;
import com.airtribe.learntrack.util.ConsolePrinter;
import com.airtribe.learntrack.util.TextTablePrinter;
import java.util.ArrayList;

public class TrainerMenu {
    private ConsoleReader reader;
    private TrainerService trainerService;
    private CourseService courseService;
    private ReceiptService receiptService;

    public TrainerMenu(ConsoleReader reader, TrainerService trainerService, CourseService courseService, ReceiptService receiptService) {
        this.reader = reader;
        this.trainerService = trainerService;
        this.courseService = courseService;
        this.receiptService = receiptService;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            try {
                printMenu();
                int choice = reader.readMenuChoice("Choose option: ");
                switch (choice) {
                    case 1:
                        addTrainer();
                        break;
                    case 2:
                        TextTablePrinter.printTrainers(trainerService.getAllTrainers());
                        break;
                    case 3:
                        TextTablePrinter.printTrainers(trainerService.getActiveTrainers());
                        break;
                    case 4:
                        System.out.println(trainerService.findTrainerById(reader.readPositiveInt("Trainer ID: ")).getTrainerCard());
                        break;
                    case 5:
                        TextTablePrinter.printTrainers(trainerService.searchTrainersByName(reader.readLine("Name keyword: ")));
                        break;
                    case 6:
                        updateTrainer();
                        break;
                    case 7:
                        OperationReceipt deactivateReceipt = trainerService.deactivateTrainer(reader.readPositiveInt("Trainer ID: "), courseService.getAllCourses());
                        System.out.println(deactivateReceipt.toDisplayBlock());
                        break;
                    case 8:
                        OperationReceipt reactivateReceipt = trainerService.reactivateTrainer(reader.readPositiveInt("Trainer ID: "));
                        System.out.println(reactivateReceipt.toDisplayBlock());
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        throw new InvalidInputException(RuleCode.INPUT_MENU_CHOICE_INVALID.format() + " Choice: " + choice);
                }
                if (!back) {
                    reader.pause();
                }
            } catch (InvalidInputException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            } catch (EntityNotFoundException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            } catch (DuplicateEntityException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            } catch (BusinessRuleViolationException exception) {
                ConsolePrinter.printError(exception.getMessage());
                reader.pause();
            }
        }
    }

    private void printMenu() {
        ConsolePrinter.printHeader("Trainer Desk");
        ConsolePrinter.printMenuOption("1", "Add trainer");
        ConsolePrinter.printMenuOption("2", "View all trainers");
        ConsolePrinter.printMenuOption("3", "View active trainers");
        ConsolePrinter.printMenuOption("4", "Search trainer by ID");
        ConsolePrinter.printMenuOption("5", "Search trainers by name");
        ConsolePrinter.printMenuOption("6", "Update trainer profile");
        ConsolePrinter.printMenuOption("7", "Safe deactivate trainer");
        ConsolePrinter.printMenuOption("8", "Reactivate trainer");
        ConsolePrinter.printMenuOption("0", "Back");
    }

    private void addTrainer() throws InvalidInputException, DuplicateEntityException {
        Trainer trainer = trainerService.addTrainer(reader.readLine("First name: "), reader.readLine("Last name: "), reader.readOptionalLine("Email (optional): "), reader.readLine("Expertise: "));
        ConsolePrinter.printSuccess(trainer.getTrainerCard());
        printLatestReceipt("TRAINER", trainer.getId());
    }

    private void updateTrainer() throws InvalidInputException, DuplicateEntityException, EntityNotFoundException {
        int id = reader.readPositiveInt("Trainer ID: ");
        Trainer trainer = trainerService.updateTrainer(id, reader.readLine("First name: "), reader.readLine("Last name: "), reader.readOptionalLine("Email (optional): "), reader.readLine("Expertise: "));
        ConsolePrinter.printSuccess(trainer.getTrainerCard());
        printLatestReceipt("TRAINER", trainer.getId());
    }

    private void printLatestReceipt(String referenceType, int referenceId) {
        ArrayList<OperationReceipt> receipts = receiptService.findReceiptsByReference(referenceType, referenceId);
        if (receipts.size() > 0) {
            System.out.println(receipts.get(receipts.size() - 1).toDisplayBlock());
        }
    }
}
