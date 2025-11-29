package command;

import insurance.Derivative;
import insurance.InsurancePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class FindPoliciesCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(FindPoliciesCommand.class);

    private final Derivative derivative;
    private final Scanner scanner;

    public FindPoliciesCommand(Derivative derivative, Scanner scanner) {
        this.derivative = derivative;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        logger.info("Виконання FindPoliciesCommand");

        if (derivative.getPolicies().isEmpty()) {
            logger.warn("Спроба пошуку, але список полісів порожній");
            System.out.println("Полісів немає.");
            return;
        }

        try {
            System.out.print("Мінімальний ризик (0-1): ");
            double minR = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Максимальний ризик (0-1): ");
            double maxR = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Максимальне зобов'язання: ");
            double maxObl = Double.parseDouble(scanner.nextLine().trim());

            logger.debug("Введені параметри пошуку: minR={}, maxR={}, maxObl={}", minR, maxR, maxObl);

            List<InsurancePolicy> found = derivative.findByParameters(minR, maxR, maxObl);

            logger.info("Знайдено {} полісів за вказаними параметрами", found.size());

            if (found.isEmpty()) {
                System.out.println("Поліси за заданими параметрами не знайдені.");
            } else {
                System.out.println("Знайдені поліси:");
                found.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            logger.error("Помилка вводу числа", e);
            System.out.println("Помилка вводу: очікувалося число.");
        }
    }
}

