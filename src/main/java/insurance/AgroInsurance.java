package insurance;

public class AgroInsurance extends InsurancePolicy {
    private String cropType; //тип культури
    private double area; //площа
    private double weatherRisk; //ризик погода

    public AgroInsurance(int id, String name, double obligation, double risk, int duration,
                         String cropType, double area, double weatherRisk) {
        super(id, name, obligation, risk, duration);
        this.cropType = cropType;
        this.area = area;
        this.weatherRisk = weatherRisk;
    }

    public String getCrop() { return cropType; }
    public double getArea() { return area; }
    public double getWeatherRisk() { return weatherRisk; }

    @Override
    public double calculatePremium() {
        return obligation * (1 + risk + weatherRisk) * (area / 10.0);
    }
}