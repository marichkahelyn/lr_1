package command;

import command.SortPoliciesCommand;
import insurance.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class SortPoliciesCommandTest {

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
    void emptyList() {
        Derivative derivative = new Derivative();
        provideInput("");
        Scanner scanner = new Scanner(System.in);

        SortPoliciesCommand cmd = new SortPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(out.toString().contains("Немає що сортувати."));
    }



    @Test
    void descendingSort() {
        Derivative derivative = new Derivative();

        derivative.addPolicy(new MedicalInsurance(1, "A", 1000, 0.3, 12, 20, "c", "s"));
        derivative.addPolicy(new MedicalInsurance(2, "B", 1000, 0.7, 12, 20, "c", "s"));
        derivative.addPolicy(new MedicalInsurance(3, "C", 1000, 0.1, 12, 20, "c", "s"));

        provideInput("false\n");
        Scanner scanner = new Scanner(System.in);

        SortPoliciesCommand cmd = new SortPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertEquals(0.7, derivative.getPolicies().get(0).getRisk());
        assertEquals(0.3, derivative.getPolicies().get(1).getRisk());
        assertEquals(0.1, derivative.getPolicies().get(2).getRisk());
    }
}

