package command;

import insurance.*;

import java.util.Scanner;

public class SaveToFileCommand implements Command {

    private final Derivative derivative;
    private final InsuranceManager manager;
    private final Scanner scanner;

    public SaveToFileCommand(Derivative derivative, InsuranceManager manager, Scanner scanner) {
        this.derivative = derivative;
        this.manager = manager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Введіть ім'я файлу для збереження: ");
        String filename = scanner.nextLine().trim();

        manager.saveToFile(filename, derivative);
    }
}