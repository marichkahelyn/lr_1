package command;

import insurance.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DeletePolicyCommandTest {

    @Test
    void testDeleteExistingPolicy() {
        Derivative derivative = new Derivative();
        InsuranceManager manager = new InsuranceManager();

        // Додаємо поліс
        InsurancePolicy policy = manager.createLifePolicy(1, "Life", 5000, 0.1, 12, 30, 10000, "good");
        derivative.addPolicy(policy);

        // Імітація вводу ID = 1
        Scanner scanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));

        DeletePolicyCommand cmd = new DeletePolicyCommand(derivative, manager, scanner);
        cmd.execute();

        assertEquals(0, derivative.getPolicies().size());
    }

    @Test
    void testDeleteNonExistingPolicy() {
        Derivative derivative = new Derivative();
        InsuranceManager manager = new InsuranceManager();

        Scanner scanner = new Scanner(new ByteArrayInputStream("99\n".getBytes()));

        DeletePolicyCommand cmd = new DeletePolicyCommand(derivative, manager, scanner);
        cmd.execute();

        assertEquals(0, derivative.getPolicies().size());
    }
}
