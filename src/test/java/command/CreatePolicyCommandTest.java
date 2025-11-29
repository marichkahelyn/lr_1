package command;

import insurance.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CreatePolicyCommandTest {

    private Derivative newDerivative() {
        return new Derivative();
    }

    private CreatePolicyCommand runWithInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(1);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();
        return cmd;
    }

    private InsurancePolicy getSinglePolicy(Derivative derivative) {
        assertEquals(1, derivative.getPolicies().size());
        return derivative.getPolicies().get(0);
    }

    // ---------- TEST 1: Медичне страхування ----------
    @Test
    void testCreateMedicalPolicy() {
        String input = String.join("\n",
                "1", "Med", "10000", "0.2", "12",
                "65", "full", "basic"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(1);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        InsurancePolicy p = getSinglePolicy(derivative);

        assertTrue(p instanceof MedicalInsurance);
        assertEquals("Med", p.getName());
        assertEquals(10000, p.getObligation());
        assertEquals(2, idCounter.get());
    }

    // ---------- TEST 2: Авто ----------
    @Test
    void testCreateAutoPolicy() {
        String input = String.join("\n",
                "2", "AutoPol", "5000", "0.3", "6",
                "sedan", "2", "true"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(10);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        InsurancePolicy p = getSinglePolicy(derivative);

        assertTrue(p instanceof AutoInsurance);
        assertEquals(10, p.getId());
        assertEquals("AutoPol", p.getName());
        assertEquals(11, idCounter.get());
    }

    // ---------- TEST 3: Майнове ----------
    @Test
    void testCreatePropertyPolicy() {
        String input = String.join("\n",
                "3", "House", "7000", "0.1", "24",
                "flat", "medium", "false"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(5);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        InsurancePolicy p = getSinglePolicy(derivative);

        assertTrue(p instanceof PropertyInsurance);
        assertEquals("House", p.getName());
        assertEquals(6, idCounter.get());
    }

    // ---------- TEST 4: Туристичне ----------
    @Test
    void testCreateTravelPolicy() {
        String input = String.join("\n",
                "4", "Trip", "20000", "0.4", "10",
                "Spain", "14", "0.15"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(3);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        InsurancePolicy p = getSinglePolicy(derivative);

        assertTrue(p instanceof TravelInsurance);
        assertEquals("Trip", p.getName());
        assertEquals(4, idCounter.get());
    }

    // ---------- TEST 5: Агро ----------
    @Test
    void testCreateAgroPolicy() {
        String input = String.join("\n",
                "5", "Agro", "15000", "0.22", "18",
                "wheat", "12.5", "0.3"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(100);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        InsurancePolicy p = getSinglePolicy(derivative);

        assertTrue(p instanceof AgroInsurance);
        assertEquals("Agro", p.getName());
        assertEquals(101, idCounter.get());
    }

    // ---------- TEST 6: Життя ----------
    @Test
    void testCreateLifePolicy() {
        String input = String.join("\n",
                "6", "LifeX", "8000", "0.12", "36",
                "45", "50000", "good"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(99);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        InsurancePolicy p = getSinglePolicy(derivative);

        assertTrue(p instanceof LifeInsurance);
        assertEquals("LifeX", p.getName());
        assertEquals(100, idCounter.get());
    }

    // ---------- TEST 7: Некоректний ввід типу ----------
    @Test
    void testWrongType() {
        String input = String.join("\n",
                "99"  // неправильний тип
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(1);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(0, derivative.getPolicies().size()); // нічого не створено
        assertEquals(1, idCounter.get()); // ID не змінено
    }

    // ---------- TEST 8: Некоректний ввід — число ----------
    @Test
    void testInvalidNumberInput() {
        String input = String.join("\n",
                "1", "X", "bad_number"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Derivative derivative = newDerivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(1);

        CreatePolicyCommand cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(0, derivative.getPolicies().size());
        assertEquals(1, idCounter.get());
    }
}