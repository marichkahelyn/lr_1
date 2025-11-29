package command;

import insurance.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CreatePolicyCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(CreatePolicyCommand.class);

    private final Derivative derivative; //список полісів
    private final InsuranceManager manager; //клас що створює поліси
    private final Scanner scanner;
    private final AtomicInteger idCounter;

    public CreatePolicyCommand(Derivative derivative,
                               InsuranceManager manager,
                               Scanner scanner,
                               AtomicInteger idCounter) {
        this.derivative = derivative;
        this.manager = manager;
        this.scanner = scanner;
        this.idCounter = idCounter;
    }

    @Override
    public void execute() {
        logger.info("Початок створення нового полісу");

        try {
            System.out.println();
            System.out.println("Оберіть тип страхування:");
            System.out.println("1. Медичне");
            System.out.println("2. Авто");
            System.out.println("3. Майнове");
            System.out.println("4. Туристичне");
            System.out.println("5. Агро");
            System.out.println("6. Життя");
            System.out.print("Ваш вибір: ");
            int type = Integer.parseInt(scanner.nextLine().trim());

            logger.info("Обрано тип страхування: {}", type);

            System.out.print("Назва полісу: ");
            String name = scanner.nextLine().trim();

            System.out.print("Страхове зобов'язання: ");
            double obligation = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Рівень ризику (0-1): ");
            double risk = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Тривалість (місяці): ");
            int duration = Integer.parseInt(scanner.nextLine().trim());

            int id = idCounter.get();

            InsurancePolicy policy; //змінна для полісу

            switch (type) {
                case 1 -> {
                    System.out.print("Вікове обмеження: ");
                    int ageLimit = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Покриття: ");
                    String coverage = scanner.nextLine().trim();
                    System.out.print("Тип послуг: ");
                    String serviceType = scanner.nextLine().trim();
                    policy = manager.createMedicalPolicy(id, name, obligation, risk, duration,
                            ageLimit, coverage, serviceType);
                }
                case 2 -> {
                    System.out.print("Тип авто: ");
                    String carType = scanner.nextLine().trim();
                    System.out.print("Кількість ДТП: ");
                    int accidents = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("КАСКО (так/ні): ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    boolean casco = input.equals("так");
                    policy = manager.createAutoPolicy(id, name, obligation, risk, duration,
                            carType, accidents, casco);
                }
                case 3 -> {
                    System.out.print("Тип майна: ");
                    String propertyType = scanner.nextLine().trim();
                    System.out.print("Ризиковість регіону (низька/середня/висока): ");
                    String regionRisk = scanner.nextLine().trim();
                    System.out.print("Захист від крадіжки (так/ні): ");
                    String theftInput = scanner.nextLine().trim().toLowerCase();
                    boolean theft = theftInput.equals("так");
                    policy = manager.createPropertyPolicy(id, name, obligation, risk, duration,
                            propertyType, regionRisk, theft);
                }
                case 4 -> {
                    System.out.print("Країна подорожі: ");
                    String country = scanner.nextLine().trim();
                    System.out.print("Тривалість поїздки (днів): ");
                    int tripDays = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Ризик нещасного випадку (0-1): ");
                    double accidentRisk = Double.parseDouble(scanner.nextLine().trim());
                    policy = manager.createTravelPolicy(id, name, obligation, risk, duration,
                            country, tripDays, accidentRisk);
                }
                case 5 -> {
                    System.out.print("Тип культури: ");
                    String crop = scanner.nextLine().trim();
                    System.out.print("Площа (га): ");
                    double area = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Ризик погоди (0-1): ");
                    double weatherRisk = Double.parseDouble(scanner.nextLine().trim());
                    policy = manager.createAgroPolicy(id, name, obligation, risk, duration,
                            crop, area, weatherRisk);
                }
                case 6 -> {
                    System.out.print("Вік: ");
                    int age = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Сума виплати: ");
                    double payout = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Стан здоров'я (хороший/поганий): ");
                    String health = scanner.nextLine().trim();
                    policy = manager.createLifePolicy(id, name, obligation, risk, duration,
                            age, payout, health);
                }
                default -> {
                    logger.warn("Невірний тип полісу: {}", type);
                    System.out.println("Невірний тип полісу.");
                    return;
                }
            }

            derivative.addPolicy(policy);
            logger.info("Поліс створено: id={}", id);

            System.out.println("Поліс створено, ID = " + id);
            idCounter.incrementAndGet();

        } catch (NumberFormatException e) {
            logger.warn("Користувач ввів некоректне число", e);
            System.out.println("Помилка вводу: очікувалося число. Спробуйте ще раз.");
        } catch (Exception e) {
            logger.error("Помилка під час створення полісу", e);
            System.out.println("Сталася помилка: " + e.getMessage());
        }
    }
}
