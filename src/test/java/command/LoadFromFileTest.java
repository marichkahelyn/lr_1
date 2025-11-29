package command;

import command.LoadFromFileCommand;
import insurance.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class LoadFromFileCommandTest {

    @Test
    void testLoadFromFileCommand() throws Exception {

        // --- 1. Готуємо тестовий файл ---
        File temp = File.createTempFile("policies", ".txt");
        temp.deleteOnExit();

        try (PrintWriter pw = new PrintWriter(temp)) {

            pw.println("# Коментар — має бути пропущений");
            pw.println("");
            pw.println("auto,CarPolicy,10000,0.3,12,Sedan,1,true"); // OK
            pw.println("medical,MedPolicy,8000,0.2,10,60,full,basic"); // OK
            pw.println("BAD LINE THAT CAUSES ERROR"); // ERROR
            pw.println("travel,Trip,20000,0.5,7,Spain,10,0.2"); // OK
        }

        // --- 2. Готуємо залежності ---
        Derivative derivative = new Derivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(1);

        // scanner "вводить" імʼя файлу
        Scanner scanner = new Scanner(temp.getAbsolutePath() + "\n");

        LoadFromFileCommand cmd =
                new LoadFromFileCommand(derivative, manager, scanner, idCounter);

        // Перехоплюємо консольний вивід
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        // --- 3. Виконуємо команду ---
        cmd.execute();

        // повертаємо stdout
        System.setOut(original);

        String output = out.toString();

        // --- 4. Перевірки ---

        // З файлу:
        // 3 коректні рядки → success = 3
        // 1 некоректний → errors = 1
        assertTrue(output.contains("Успішно: 3"));
        assertTrue(output.contains("Помилки: 1"));

        // Перевіряємо кількість доданих полісів
        assertEquals(3, derivative.getPolicies().size());

        // Перевіряємо, що idCounter інкрементований правильно:
        // Починався з 1 → після 3 успішних = 4
        assertEquals(4, idCounter.get());

        // Перевіримо, що типи полісів правильні
        assertTrue(derivative.getPolicies().get(0) instanceof AutoInsurance);
        assertTrue(derivative.getPolicies().get(1) instanceof MedicalInsurance);
        assertTrue(derivative.getPolicies().get(2) instanceof TravelInsurance);
    }
}