package insurance;

public class LifeInsurance extends InsurancePolicy {
    private int age;
    private double payoutSum;
    private String healthStatus;

    public LifeInsurance(int id, String name, double obligation, double risk, int duration,
                         int age, double payoutSum, String healthStatus) {
        super(id, name, obligation, risk, duration);
        this.age = age;
        this.payoutSum = payoutSum;
        this.healthStatus = healthStatus;
    }

    public int getAge() { return age; }
    public double getPayout() { return payoutSum; }
    public String getHealth() { return healthStatus; }

    @Override
    public double calculatePremium() {
        double ageFactor = (age > 50) ? 1.25 : 1.0;
        double healthFactor = healthStatus.equalsIgnoreCase("поганий") ? 1.4 : 1.0;
        return obligation * (1 + risk) * ageFactor * healthFactor;
    }
}