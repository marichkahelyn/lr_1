package insurance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertyInsuranceTest {

    // Тест 1: Базовий випадок (low risk, no theft)
    @Test
    void testPremiumLowRiskNoTheft() {
        // Arrange
        // Логіка: obligation * (1 + risk) * regionFactor * theftFactor
        // region = "low" (factor 1.0), theft = false (factor 1.0)
        PropertyInsurance policy = new PropertyInsurance(
                50, "Basic Property", 200000, 0.1, 24, "Apartment", "low", false);

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 200000 * (1 + 0.1) * 1.0 * 1.0 = 220000.0
        assertEquals(220000.0, premium, 0.001);
    }

    //  З захистом від крадіжки
    @Test
    void testPremiumWithTheftProtection() {
        // Arrange
        // region = "low" (factor 1.0), theft = true (factor 1.1)
        PropertyInsurance policy = new PropertyInsurance(
                52, "Secure Property", 200000, 0.1, 24, "Apartment", "low", true);

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 200000 * (1 + 0.1) * 1.0 * 1.1 = 242000.0
        assertEquals(242000.0, premium, 0.001);
    }

}