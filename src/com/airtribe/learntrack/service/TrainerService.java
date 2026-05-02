package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.OperationReceipt;
import com.airtribe.learntrack.entity.Person;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.entity.enums.ActionType;
import com.airtribe.learntrack.entity.enums.RuleCode;
import com.airtribe.learntrack.exception.BusinessRuleViolationException;
import com.airtribe.learntrack.exception.DuplicateEntityException;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.policy.DeactivationPolicy;
import com.airtribe.learntrack.util.IdGenerator;
import com.airtribe.learntrack.util.InputValidator;
import java.time.LocalDate;
import java.util.ArrayList;

public class TrainerService {
    private ArrayList<Trainer> trainers = new ArrayList<Trainer>();
    private ActionLogService actionLogService;
    private ReceiptService receiptService;

    public TrainerService(ActionLogService actionLogService, ReceiptService receiptService) {
        this.actionLogService = actionLogService;
        this.receiptService = receiptService;
    }

    public Trainer addTrainer(String firstName, String lastName, String email, String expertise) throws InvalidInputException, DuplicateEntityException {
        InputValidator.requireNonBlank(firstName, "trainer first name");
        InputValidator.requireNonBlank(lastName, "trainer last name");
        InputValidator.requireNonBlank(expertise, "trainer expertise");
        InputValidator.validateEmailIfPresent(email);
        String normalizedEmail = InputValidator.normalizeEmail(email);
        if (normalizedEmail.length() > 0 && emailExistsForAnotherTrainer(normalizedEmail, 0)) {
            throw new DuplicateEntityException(RuleCode.TRAINER_EMAIL_UNIQUE.format() + " Email: " + normalizedEmail);
        }
        Trainer trainer = new Trainer(
                IdGenerator.getNextTrainerId(),
                InputValidator.normalizeName(firstName),
                InputValidator.normalizeName(lastName),
                normalizedEmail,
                InputValidator.normalizeName(expertise),
                true,
                LocalDate.now());
        trainers.add(trainer);
        actionLogService.record(ActionType.TRAINER_CREATED, "Trainer profile created for " + trainer.getFullName(), "Admin", "TRAINER", trainer.getId());
        receiptService.issueReceipt("CREATE_TRAINER", "CREATED", "Trainer " + trainer.getFullName() + " is available for course coverage.", RuleCode.TRAINER_EMAIL_UNIQUE.format(), "Assign trainer to an active course.", "TRAINER", trainer.getId());
        return trainer;
    }

    public ArrayList<Trainer> getAllTrainers() {
        return new ArrayList<Trainer>(trainers);
    }

    public ArrayList<Trainer> getActiveTrainers() {
        ArrayList<Trainer> activeTrainers = new ArrayList<Trainer>();
        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).isActive()) {
                activeTrainers.add(trainers.get(i));
            }
        }
        return activeTrainers;
    }

    public Trainer findTrainerById(int id) throws EntityNotFoundException {
        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).getId() == id) {
                return trainers.get(i);
            }
        }
        throw new EntityNotFoundException("Trainer #" + id + " was not found.");
    }

    public ArrayList<Trainer> searchTrainersByName(String keyword) {
        ArrayList<Trainer> matches = new ArrayList<Trainer>();
        String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).getFullName().toLowerCase().indexOf(normalized) >= 0) {
                matches.add(trainers.get(i));
            }
        }
        return matches;
    }

    public Trainer updateTrainer(int id, String firstName, String lastName, String email, String expertise) throws EntityNotFoundException, InvalidInputException, DuplicateEntityException {
        Trainer trainer = findTrainerById(id);
        InputValidator.requireNonBlank(firstName, "trainer first name");
        InputValidator.requireNonBlank(lastName, "trainer last name");
        InputValidator.requireNonBlank(expertise, "trainer expertise");
        InputValidator.validateEmailIfPresent(email);
        String normalizedEmail = InputValidator.normalizeEmail(email);
        if (normalizedEmail.length() > 0 && emailExistsForAnotherTrainer(normalizedEmail, id)) {
            throw new DuplicateEntityException(RuleCode.TRAINER_EMAIL_UNIQUE.format() + " Email: " + normalizedEmail);
        }
        trainer.setFirstName(InputValidator.normalizeName(firstName));
        trainer.setLastName(InputValidator.normalizeName(lastName));
        trainer.setEmail(normalizedEmail);
        trainer.setExpertise(InputValidator.normalizeName(expertise));
        actionLogService.record(ActionType.TRAINER_UPDATED, "Trainer profile updated for " + trainer.getFullName(), "Admin", "TRAINER", trainer.getId());
        receiptService.issueReceipt("UPDATE_TRAINER", "UPDATED", "Trainer profile refreshed for " + trainer.getFullName() + ".", RuleCode.TRAINER_EMAIL_UNIQUE.format(), "Check Trainer Coverage Report.", "TRAINER", trainer.getId());
        return trainer;
    }

    public OperationReceipt deactivateTrainer(int id, ArrayList<Course> courses) throws EntityNotFoundException, BusinessRuleViolationException {
        Trainer trainer = findTrainerById(id);
        new DeactivationPolicy().validateTrainerCanBeDeactivated(trainer, courses);
        trainer.setActive(false);
        actionLogService.record(ActionType.TRAINER_DEACTIVATED, "Safe Deactivation completed for trainer " + trainer.getFullName(), "Admin", "TRAINER", trainer.getId());
        return receiptService.issueReceipt("SAFE_DEACTIVATE_TRAINER", "DEACTIVATED", "Trainer was deactivated after active course assignment checks.", RuleCode.DEACTIVATE_TRAINER_ASSIGNED_TO_ACTIVE_COURSE.format(), "Reactivate before assigning to another course.", "TRAINER", trainer.getId());
    }

    public OperationReceipt reactivateTrainer(int id) throws EntityNotFoundException {
        Trainer trainer = findTrainerById(id);
        trainer.setActive(true);
        actionLogService.record(ActionType.TRAINER_REACTIVATED, "Trainer reactivated for " + trainer.getFullName(), "Admin", "TRAINER", trainer.getId());
        return receiptService.issueReceipt("REACTIVATE_TRAINER", "REACTIVATED", "Trainer returned to active status.", RuleCode.TRAINER_INACTIVE_ASSIGNMENT.format(), "Assign trainer to a course if coverage is needed.", "TRAINER", trainer.getId());
    }

    public int countActiveTrainers() {
        return getActiveTrainers().size();
    }

    public int countInactiveTrainers() {
        int count = 0;
        for (int i = 0; i < trainers.size(); i++) {
            if (!trainers.get(i).isActive()) {
                count++;
            }
        }
        return count;
    }

    public boolean emailExistsForAnotherTrainer(String email, int currentTrainerId) {
        String normalizedEmail = InputValidator.normalizeEmail(email);
        if (normalizedEmail.length() == 0) {
            return false;
        }
        for (int i = 0; i < trainers.size(); i++) {
            Trainer trainer = trainers.get(i);
            if (trainer.getId() != currentTrainerId && trainer.getEmail() != null && trainer.getEmail().equalsIgnoreCase(normalizedEmail)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Person> getTrainersAsPeople() {
        ArrayList<Person> people = new ArrayList<Person>();
        for (int i = 0; i < trainers.size(); i++) {
            people.add(trainers.get(i));
        }
        return people;
    }
}
