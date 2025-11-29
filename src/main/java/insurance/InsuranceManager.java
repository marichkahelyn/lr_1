package insurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class InsuranceManager {

    private static final Logger logger = LoggerFactory.getLogger(InsuranceManager.class);

    // ---------- СТВОРЕННЯ ПОЛІСІВ ----------

    public InsurancePolicy createMedicalPolicy(int id, String name, double obligation, double risk, int duration,
                                               int ageLimit, String coverage, String serviceType) {

        logger.info("Створення MedicalInsurance [id={}, name={}]", id, name);

        return new MedicalInsurance(id, name, obligation, risk, duration, ageLimit, coverage, serviceType);
    }

    public InsurancePolicy createAutoPolicy(int id, String name, double obligation, double risk, int duration,
                                            String carType, int accidents, boolean casco) {

        logger.info("Створення AutoInsurance [id={}, name={}, carType={}]", id, name, carType);

        return new AutoInsurance(id, name, obligation, risk, duration, carType, accidents, casco);
    }

    public InsurancePolicy createPropertyPolicy(int id, String name, double obligation, double risk, int duration,
                                                String propertyType, String regionRisk, boolean theft) {

        logger.info("Створення PropertyInsurance [id={}, name={}, propertyType={}]", id, name, propertyType);

        return new PropertyInsurance(id, name, obligation, risk, duration, propertyType, regionRisk, theft);
    }

    public InsurancePolicy createTravelPolicy(int id, String name, double obligation, double risk, int duration,
                                              String country, int tripDays, double accidentRisk) {

        logger.info("Створення TravelInsurance [id={}, country={}, days={}]", id, country, tripDays);

        return new TravelInsurance(id, name, obligation, risk, duration, country, tripDays, accidentRisk);
    }

    public InsurancePolicy createAgroPolicy(int id, String name, double obligation, double risk, int duration,
                                            String crop, double area, double weatherRisk) {

        logger.info("Створення AgroInsurance [id={}, crop={}, area={}]", id, crop, area);

        return new AgroInsurance(id, name, obligation, risk, duration, crop, area, weatherRisk);
    }

    public InsurancePolicy createLifePolicy(int id, String name, double obligation, double risk, int duration,
                                            int age, double payout, String health) {

        logger.info("Створення LifeInsurance [id={}, name={}, age={}]", id, name, age);

        return new LifeInsurance(id, name, obligation, risk, duration, age, payout, health);
    }


    // ---------- ВИДАЛЕННЯ ----------

    public boolean removePolicyById(int id, Derivative derivative) {
        logger.info("Спроба видалити поліс id={}", id);

        boolean removed = derivative.getPolicies().removeIf(p -> p.getId() == id);

        if (removed)
            logger.info("Поліс id={} успішно видалено", id);
        else
            logger.warn("Поліс id={} НЕ знайдено для видалення", id);

        return removed;
    }


    // ---------- ГЕНЕРАЦІЯ ЗВІТУ ----------

    public void generateReport(Derivative derivative) {

        logger.info("Генерація звіту (кількість полісів: {})",
                derivative.getPolicies().size());

        System.out.println("\n===== Звіт по Деривативу =====");

        for (InsurancePolicy p : derivative.getPolicies()) {
            String line = p + " (Премія: " + String.format("%.2f", p.calculatePremium()) + ")";
            System.out.println(line);
        }

        double total = derivative.calculateTotalValue();
        System.out.println("--------------------------------");
        System.out.println("Загальна вартість (сума премій): " + String.format("%.2f", total));

        logger.info("Звіт сформовано. Загальна премія = {}", total);
    }


    // ---------- ПАРСИНГ РЯДКА ----------
    public InsurancePolicy parsePolicyFromLine(String line, int id) {

        logger.info("Парсинг рядка у поліс: {}", line);

        String[] d = line.split(",");
        if (d.length < 5) {
            logger.error("Помилка парсингу: недостатньо параметрів у '{}'", line);
            throw new IllegalArgumentException("Недостатньо базових даних");
        }

        String type = d[0].trim().toLowerCase();
        String name = d[1].trim();

        try {
            double obligation = Double.parseDouble(d[2].trim());
            double risk = Double.parseDouble(d[3].trim());
            int duration = Integer.parseInt(d[4].trim());

            logger.debug("Тип={}, name={}, obligation={}, risk={}, duration={}",
                    type, name, obligation, risk, duration);

            switch (type) {
                case "auto":
                    if (d.length < 8)
                        throw new IllegalArgumentException("Недостатньо даних для auto");
                    return createAutoPolicy(id, name, obligation, risk, duration,
                            d[5].trim(),
                            Integer.parseInt(d[6].trim()),
                            Boolean.parseBoolean(d[7].trim())
                    );

                case "medical":
                    if (d.length < 8)
                        throw new IllegalArgumentException("Недостатньо даних для medical");
                    return createMedicalPolicy(id, name, obligation, risk, duration,
                            Integer.parseInt(d[5].trim()),
                            d[6].trim(),
                            d[7].trim()
                    );

                case "property":
                    if (d.length < 8)
                        throw new IllegalArgumentException("Недостатньо даних для property");
                    return createPropertyPolicy(id, name, obligation, risk, duration,
                            d[5].trim(),
                            d[6].trim(),
                            Boolean.parseBoolean(d[7].trim())
                    );

                case "travel":
                    if (d.length < 8)
                        throw new IllegalArgumentException("Недостатньо даних для travel");
                    return createTravelPolicy(id, name, obligation, risk, duration,
                            d[5].trim(),
                            Integer.parseInt(d[6].trim()),
                            Double.parseDouble(d[7].trim())
                    );

                case "agro":
                    if (d.length < 8)
                        throw new IllegalArgumentException("Недостатньо даних для agro");
                    return createAgroPolicy(id, name, obligation, risk, duration,
                            d[5].trim(),
                            Double.parseDouble(d[6].trim()),
                            Double.parseDouble(d[7].trim())
                    );

                case "life":
                    if (d.length < 8)
                        throw new IllegalArgumentException("Недостатньо даних для life");
                    return createLifePolicy(id, name, obligation, risk, duration,
                            Integer.parseInt(d[5].trim()),
                            Double.parseDouble(d[6].trim()),
                            d[7].trim()
                    );

                default:
                    logger.error("Невідомий тип полісу '{}'", type);
                    throw new IllegalArgumentException("Невідомий тип полісу: " + type);
            }

        } catch (NumberFormatException e) {
            logger.error("Помилка перетворення числа у '{}'", line, e);
            throw new IllegalArgumentException("Некоректні числові значення");
        }
    }


    // ---------- ЗАПИС У ФАЙЛ ----------

    public void saveToFile(String filename, Derivative derivative) {

        logger.info("Спроба зберегти дані у файл: {}", filename);

        try (FileWriter fw = new FileWriter(filename)) {

            for (InsurancePolicy p : derivative.getPolicies()) {
                fw.write(convertPolicyToLine(p) + "\n"); //формуємо рядок, аналогічний тому який парсимо
            }

            logger.info("Файл '{}' успішно збережено ({} полісів)",
                    filename, derivative.getPolicies().size());

            System.out.println("Дані успішно збережено у " + filename);

        } catch (IOException e) {
            logger.error("Помилка запису у файл '{}': {}", filename, e.getMessage());
            System.out.println("Помилка запису у файл: " + e.getMessage());
        }
    }


    // ---------- КОНВЕРТАЦІЯ У ФОРМАТ ФАЙЛУ ----------

    protected String convertPolicyToLine(InsurancePolicy p) {

        logger.debug("Конвертація полісу id={} у рядок", p.getId());

        if (p instanceof AutoInsurance a) {
            return String.join(",",
                    "auto", a.getName(), a.getObligation() + "", a.getRisk() + "", a.getDuration() + "",
                    a.getCarType(), a.getAccidents() + "", a.isCasco() + "");
        }

        if (p instanceof MedicalInsurance m) {
            return String.join(",",
                    "medical", m.getName(), m.getObligation() + "", m.getRisk() + "", m.getDuration() + "",
                    m.getAgeLimit() + "", m.getCoverage(), m.getServiceType());
        }

        if (p instanceof PropertyInsurance pr) {
            return String.join(",",
                    "property", pr.getName(), pr.getObligation() + "", pr.getRisk() + "", pr.getDuration() + "",
                    pr.getPropertyType(), pr.getRegionRisk(), pr.isTheftProtection() + "");
        }

        if (p instanceof TravelInsurance t) {
            return String.join(",",
                    "travel", t.getName(), t.getObligation() + "", t.getRisk() + "", t.getDuration() + "",
                    t.getCountry(), t.getTripDays() + "", t.getAccidentRisk() + "");
        }

        if (p instanceof AgroInsurance ag) {
            return String.join(",",
                    "agro", ag.getName(), ag.getObligation() + "", ag.getRisk() + "", ag.getDuration() + "",
                    ag.getCrop(), ag.getArea() + "", ag.getWeatherRisk() + "");
        }

        if (p instanceof LifeInsurance l) {
            return String.join(",",
                    "life", l.getName(), l.getObligation() + "", l.getRisk() + "", l.getDuration() + "",
                    l.getAge() + "", l.getPayout() + "", l.getHealth());
        }

        logger.warn("Невідомий тип полісу при конвертації: {}", p);
        return "";
    }


}