package command;

import insurance.Derivative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class SortPoliciesCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(SortPoliciesCommand.class);

    private final Derivative derivative;
    private final Scanner scanner;

    public SortPoliciesCommand(Derivative derivative, Scanner scanner) {
        this.derivative = derivative;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        logger.info("Виконання SortPoliciesCommand");

        if (derivative.getPolicies().isEmpty()) {
            logger.warn("Спроба сортування, але поліси відсутні");
            System.out.println("Немає що сортувати.");
            return;
        }

        System.out.print("Сортувати за зростанням ризику? (так/ні): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        boolean asc = ans.equals("так");

        logger.debug("Вибрано порядок сортування asc={}", asc);

        derivative.sortByRisk(asc);

        logger.info("Сортування виконано успішно");

        System.out.println("Поліси відсортовано.");
    }
}