package insurance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsurancePolicyTest {
    // Використовуємо MedicalInsurance як простий приклад базового полісу
    private MedicalInsurance policy = new MedicalInsurance(
            1, "Test Policy", 50000, 0.5, 12, 70, "Standard", "All");

    @Test
    void testPolicyInitializationAndGetters() {
        assertEquals(1, policy.getId());
        assertEquals("Test Policy", policy.getName());
        assertEquals(0.5f, policy.getRisk(), 0.001);
        assertEquals(12, policy.getDuration());
        assertNotNull(policy);
    }


    @Test
    void testPremiumIsCalculated() {
        // Перевіряємо, що метод calculatePremium повертає результат
        // (навіть якщо логіка розрахунку знаходиться у підкласі)
        double premium = policy.calculatePremium();
        assertTrue(premium > 0, "Премія має бути більша за 0");
    }
}
