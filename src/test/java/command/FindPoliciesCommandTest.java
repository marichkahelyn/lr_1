package command;

import command.FindPoliciesCommand;
import insurance.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class FindPoliciesCommandTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void emptyListShowsMessage() {
        Derivative derivative = new Derivative();
        provideInput("");
        Scanner scanner = new Scanner(System.in);

        FindPoliciesCommand cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(out.toString().contains("Полісів немає."));
    }

    @Test
    void validSearchFindsOne() {
        Derivative derivative = new Derivative();

        derivative.addPolicy(new MedicalInsurance(1, "P1", 5000, 0.2, 12, 25, "cov", "srv"));
        derivative.addPolicy(new MedicalInsurance(2, "P2", 9000, 0.6, 12, 25, "cov", "srv"));

        provideInput("0.1\n0.3\n6000\n");
        Scanner scanner = new Scanner(System.in);

        FindPoliciesCommand cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        String output = out.toString();
        assertTrue(output.contains("Знайдені поліси"));
        assertTrue(output.contains("P1"));
        assertFalse(output.contains("P2"));
    }

    @Test
    void numberFormatError() {
        Derivative derivative = new Derivative();
        derivative.addPolicy(new MedicalInsurance(1, "P1", 1000, 0.2, 12, 20, "c", "s"));

        provideInput("aaa\n");
        Scanner scanner = new Scanner(System.in);

        FindPoliciesCommand cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(out.toString().contains("Помилка вводу: очікувалося число."));
    }

    @Test
    void noPoliciesMatch() {
        Derivative derivative = new Derivative();
        derivative.addPolicy(new MedicalInsurance(1, "P1", 1000, 0.9, 12, 20, "c", "s"));

        provideInput("0.0\n0.3\n500\n");
        Scanner scanner = new Scanner(System.in);

        FindPoliciesCommand cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(out.toString().contains("не знайдені"));
    }
}