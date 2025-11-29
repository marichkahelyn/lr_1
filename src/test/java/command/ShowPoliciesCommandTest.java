package command;

import insurance.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ShowPoliciesCommandTest {

    @Test
    void testShowPoliciesEmpty() {
        Derivative derivative = new Derivative();
        ShowPoliciesCommand cmd = new ShowPoliciesCommand(derivative);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        cmd.execute();

        System.setOut(original);

        assertTrue(out.toString().contains("Полісів немає."));
    }

    @Test
    void testShowPoliciesNonEmpty() {
        Derivative derivative = new Derivative();

        // додаємо простий тестовий поліс
        InsurancePolicy p = new AutoInsurance(
                1, "TestAuto", 10000, 0.2, 12,
                "Sedan", 1, true
        );
        derivative.addPolicy(p);

        ShowPoliciesCommand cmd = new ShowPoliciesCommand(derivative);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        cmd.execute();

        System.setOut(original);

        String result = out.toString();

        assertTrue(result.contains("Всі поліси:"));
        assertTrue(result.contains("TestAuto"));
    }
}