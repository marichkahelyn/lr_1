package model;

/**
 * Клас, що описує автомобіль.
 */
public class Car {
    private int id;
    private String model;
    private int year;
    private double price;
    private String regNumber;

    // Конструктор за замовчуванням
    /** Конструктор за замовчуванням */
    public Car() {
    }

    // Конструктор з параметрами
    /**
     * Конструктор, що ініціалізує всі поля автомобіля.
     *
     * @param id        ідентифікатор автомобіля
     * @param model     модель автомобіля
     * @param year      рік випуску
     * @param price     ціна автомобіля
     * @param regNumber реєстраційний номер
     */
    public Car(int id, String model, int year, double price, String regNumber) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.price = price;
        this.regNumber = regNumber;
    }

    // Гетери
    public int getId() { return id; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getRegNumber() { return regNumber; }

    // Сетери
    public void setId(int id) { this.id = id; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(double price) { this.price = price; }
    public void setRegNumber(String regNumber) { this.regNumber = regNumber; }

    @Override
    public String toString() {
        return String.format("%-5d | %-10s | %-6d | %-10.2f | %-10s",
                id, model, year, price, regNumber);
    }
}