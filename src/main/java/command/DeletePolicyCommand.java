package command;

import insurance.Derivative;
import insurance.InsuranceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class DeletePolicyCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(DeletePolicyCommand.class);

    private final Derivative derivative;
    private final InsuranceManager manager;
    private final Scanner scanner;

    public DeletePolicyCommand(Derivative derivative, InsuranceManager manager, Scanner scanner) {
        this.derivative = derivative;
        this.manager = manager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Введіть ID полісу для видалення: ");
        String s = scanner.nextLine();

        logger.info("Спроба видалення полісу з id={}", s);

        int id;
        try {
            id = Integer.parseInt(s);
        } catch (Exception e) {
            logger.warn("Користувач ввів неправильний ID: {}", s);
            System.out.println("Помилка: введіть число.");
            return;
        }

        if (manager.removePolicyById(id, derivative)) {
            logger.info("Поліс id={} успішно видалено", id);
            System.out.println("Поліс успішно видалено.");
        } else {
            logger.warn("Поліс id={} не знайдено", id);
            System.out.println("Поліс з таким ID не знайдено.");
        }
    }
}