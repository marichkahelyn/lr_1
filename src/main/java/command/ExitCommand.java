package command;

import insurance.*;

import java.util.Scanner;

public class ExitCommand implements Command {

    private final Derivative derivative;
    private final InsuranceManager manager;
    private final Scanner scanner;

    public ExitCommand(Derivative derivative, InsuranceManager manager, Scanner scanner) {
        this.derivative = derivative;
        this.manager = manager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Бажаєте зберегти дані перед виходом? (так/ні): ");
        String ans = scanner.nextLine().trim().toLowerCase();

        if (ans.equals("так")) {
            System.out.print("Введіть ім'я файлу: ");
            String filename = scanner.nextLine().trim();
            manager.saveToFile(filename, derivative);
        }

        System.out.println("Вихід з програми.");
        System.exit(0);
    }
}