package insurance;

public class PropertyInsurance extends InsurancePolicy {
    private String propertyType;
    private String regionRisk;
    private boolean theftProtection;

    public PropertyInsurance(int id, String name, double obligation, double risk, int duration,
                             String propertyType, String regionRisk, boolean theftProtection) {
        super(id, name, obligation, risk, duration);
        this.propertyType = propertyType;
        this.regionRisk = regionRisk;
        this.theftProtection = theftProtection;
    }

    public String getPropertyType() { return propertyType; }
    public String getRegionRisk() { return regionRisk; }
    public boolean isTheftProtection() { return theftProtection; }

    @Override
    public double calculatePremium() {
        double regionFactor = switch (regionRisk.toLowerCase()) {
            case "високий" -> 1.3;
            case "середній" -> 1.15;
            default -> 1.0;
        };

        double theftFactor = theftProtection ? 1.1 : 1.0;

        return obligation * (1 + risk) * regionFactor * theftFactor;
    }

    @Override
    public String toString() {
        return String.format("Майнове страхування [ID=%d, Risk=%.2f, Obligation=%.2f, Type=%s]",
                id, risk, obligation, propertyType);
    }
}