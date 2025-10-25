package main;

import model.Car;

import java.io.*;
import java.time.Year;
import java.util.*;

/**
 * Клас {@code Main} — головний клас програми, що дозволяє працювати зі списком автомобілів.
 * Підтримує зчитування/запис з файлу, ручне введення, пошук, фільтрацію та видалення автомобілів.
 */
public class Main {
    /**
     * Точка входу у програму.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Шлях до файлу з даними про автомобілі
        String filePath = "resources/cars.txt";
        List<Car> cars = new ArrayList<>();

        System.out.println("Оберіть джерело даних:");
        System.out.println("1 — Зчитати автомобілі з файлу");
        System.out.println("2 — Ввести автомобілі вручну");
        System.out.print("Ваш вибір: ");
        int choice = sc.nextInt();
        sc.nextLine(); // очищення буфера після введення числа

        // Зчитування з файлу
        if (choice == 1) {
            File file = new File(filePath);

            // Якщо файл не існує або порожній — створюємо і заповнюємо випадковими даними
            if (!file.exists() || file.length() == 0) {
                generateCarsFile(filePath, 250);
                System.out.println("Файл створено і заповнено випадковими даними!");
            }

            // Зчитування даних з файлу у список
            cars = readCarsFromFile(filePath);
            System.out.println("\n=== Всі автомобілі з файлу ===");
            printCarsTable(cars);

            // === Ручне введення даних користувачем ===
        } else if (choice == 2) {
            System.out.print("Скільки автомобілів бажаєте ввести? ");
            int count = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= count; i++) {
                System.out.println("\n--- Автомобіль #" + i + " ---");
                System.out.print("Модель: ");
                String model = sc.nextLine();
                System.out.print("Рік випуску: ");
                int year = sc.nextInt();
                System.out.print("Ціна: ");
                double price = sc.nextDouble();
                sc.nextLine(); // очищення після числа
                System.out.print("Реєстраційний номер: ");
                String regNumber = sc.nextLine();

                // Додаємо введений автомобіль у список
                cars.add(new Car(i, model, year, price, regNumber));
            }

            System.out.println("\n=== Введені автомобілі ===");
            printCarsTable(cars);
        } else {
            System.out.println("Невірний вибір. Завершення програми.");
            return;
        }

        // Головне меню програми
        while (true) {
            System.out.println("\n==============================");
            System.out.println("МЕНЮ:");
            System.out.println("1 — Пошук автомобіля за ID");
            System.out.println("2 — Додати новий автомобіль");
            System.out.println("3 — Видалити автомобіль за ID");
            System.out.println("4 — Показати всі автомобілі");
            System.out.println("5 — Список автомобілів заданої моделі");
            System.out.println("6 — Список автомобілів заданої моделі, які експлуатуються більше N років");
            System.out.println("7 — Список автомобілів заданого року випуску, ціна яких більше вказаної");
            System.out.println("0 — Вийти");
            System.out.print("Ваш вибір: ");
            String option = sc.next();

            switch (option) {
                // Пошук за ID
                case "1" -> {
                    System.out.print("Введіть ID автомобіля: ");
                    int idSearch = sc.nextInt();
                    sc.nextLine();
                    Optional<Car> found = cars.stream()
                            .filter(c -> c.getId() == idSearch)
                            .findFirst();
                    if (found.isPresent()) {
                        System.out.println("Знайдено: " + found.get());
                    } else {
                        System.out.println("Автомобіль з таким ID не знайдено.");
                    }
                }

                // Додавання нового автомобіля
                case "2" -> {
                    sc.nextLine();
                    System.out.println("=== Додавання нового автомобіля ===");
                    System.out.print("Модель: ");
                    String model = sc.nextLine();
                    System.out.print("Рік випуску: ");
                    int yearAdd = sc.nextInt();
                    System.out.print("Ціна: ");
                    double priceAdd = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Реєстраційний номер: ");
                    String reg = sc.nextLine();

                    // Автоматично генеруємо ID
                    int id = cars.isEmpty() ? 1 : cars.get(cars.size() - 1).getId() + 1;
                    cars.add(new Car(id, model, yearAdd, priceAdd, reg));
                    System.out.println("Автомобіль додано.");

                }

                // Видалення за ID
                case "3" -> {
                    System.out.print("Введіть ID автомобіля для видалення: ");
                    int idDel = sc.nextInt();
                    sc.nextLine();
                    boolean removed = cars.removeIf(c -> c.getId() == idDel);
                    if (removed) {
                        System.out.println("Автомобіль видалено.");
                    } else {
                        System.out.println("Не знайдено автомобіля з таким ID.");
                    }
                }

                // Вивід усіх автомобілів
                case "4" -> {
                    System.out.println("=== Усі автомобілі ===");
                    printCarsTable(cars);
                }

                // (a) Список автомобілів заданої моделі
                case "5" -> {
                    sc.nextLine();
                    System.out.print("\n(a) Введіть модель автомобіля для пошуку: ");
                    String modelA = sc.nextLine().trim();
                    if (!isModelPresent(cars, modelA)) {
                        System.out.println("У списку немає автомобілів моделі '" + modelA + "'.");
                    } else {
                        System.out.println("\nАвтомобілі моделі " + modelA + ":");
                        printByModel(cars, modelA);
                    }
                }

                // (b) Список автомобілів заданої моделі, які експлуатуються більше N років
                case "6" -> {
                    sc.nextLine();
                    System.out.print("\n(b) Введіть модель автомобіля: ");
                    String modelB = sc.nextLine().trim();
                    if (!isModelPresent(cars, modelB)) {
                        System.out.println("У списку немає автомобілів моделі '" + modelB + "'.");
                    } else {
                        System.out.print("Введіть кількість років експлуатації: ");
                        int n = sc.nextInt();
                        sc.nextLine();
                        System.out.println("\nАвтомобілі моделі " + modelB + ", які експлуатуються більше " + n + " років:");
                        printByModelAndAge(cars, modelB, n);
                    }
                }

                // (c) Список автомобілів заданого року випуску, ціна яких більше вказаної
                case "7" -> {
                    int year;
                    while (true) {
                        System.out.print("Рік випуску: ");
                        if (sc.hasNextInt()) {
                            year = sc.nextInt();
                            int currentYear = Year.now().getValue();
                            if (year >= 1950 && year <= currentYear) {
                                break;
                            } else {
                                System.out.println("Рік має бути між 1950 і " + currentYear + ".");
                            }
                        } else {
                            sc.next(); // очищення некоректного введення
                        }
                    }

                    System.out.print("Введіть мінімальну ціну: ");
                    double price = sc.nextDouble();

                    System.out.println("\nАвтомобілі " + year + " року з ціною більше " + price + ":");
                    printByYearAndPrice(cars, year, price);
                }

                // Вихід з програми
                case "0" -> {
                    System.out.println("Вихід із програми.");
                    saveCarsToFile(filePath, cars);
                    return;
                }

                // Якщо користувач ввів неправильний пункт
                default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void printCarsTable(List<Car> cars) {
        System.out.printf("%-5s | %-10s | %-6s | %-10s | %-10s%n",
                "ID", "Модель", "Рік", "Ціна", "Номер");
        System.out.println("-----------------------------------------------------------");

        for (Car c : cars) {
            System.out.println(c);  // тут автоматично викликається toString()
        }
    }

    //  Перевірка наявності моделі в списку
    private static boolean isModelPresent(List<Car> cars, String modelA) {
        for (Car car : cars) {
            if (car.getModel().equalsIgnoreCase(modelA)) {
                return true;
            }
        }
        return false;
    }

    // Генерація випадкових автомобілів для створення файлу
    public static void generateCarsFile(String filePath, int count) {
        String[] models = {"Toyota", "BMW", "Audi", "Ford", "Honda", "Mazda", "Kia", "Hyundai", "Nissan", "Mercedes", "Porsche", "Volvo"};
        Random random = new Random();

        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 1; i <= count; i++) {
                int year = 1950 + random.nextInt(76); // від 1950 до 2025 року
                double price = 10000 + random.nextInt(40000);
                String model = models[random.nextInt(models.length)];
                String regNumber = generateRegNumber(random);

                // Запис кожного авто в один рядок через кому
                writer.write(i + "," + model + "," + year + "," + price + "," + regNumber + "\n");
            }
        } catch (IOException e) {
            System.err.println("Помилка створення файлу: " + e.getMessage());
        }
    }

    // Генерація випадкового реєстраційного номера
    /**
     * Генерує випадкові автомобілі і записує у файл.
     */
    private static String generateRegNumber(Random random) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        sb.append(letters.charAt(random.nextInt(letters.length())));
        sb.append(letters.charAt(random.nextInt(letters.length())));
        sb.append(String.format("%04d", random.nextInt(10000)));
        sb.append(letters.charAt(random.nextInt(letters.length())));
        sb.append(letters.charAt(random.nextInt(letters.length())));
        return sb.toString();
    }

    //  Зчитування автомобілів з файлу
    /**
     * Зчитує автомобілі з файлу у список.
     */
    public static List<Car> readCarsFromFile(String filePath) {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Зчитування кожного рядка (один рядок = один автомобіль)
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0].trim());
                    String model = parts[1].trim();
                    int year = Integer.parseInt(parts[2].trim());
                    double price = Double.parseDouble(parts[3].trim());
                    String regNumber = parts[4].trim();
                    cars.add(new Car(id, model, year, price, regNumber));
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
        }
        return cars;
    }

    // Методи фільтрації для критеріїв
    /**
     * Виводить автомобілі заданої моделі.
     */
    public static void printByModel(List<Car> cars, String model) {
        cars.stream()
                .filter(c -> c.getModel().equalsIgnoreCase(model))
                .forEach(System.out::println);
    }

    public static void printByModelAndAge(List<Car> cars, String model, int years) {
        int currentYear = Year.now().getValue();
        cars.stream()
                .filter(c -> c.getModel().equalsIgnoreCase(model))
                .filter(c -> (currentYear - c.getYear()) > years)
                .forEach(System.out::println);
    }

    public static void printByYearAndPrice(List<Car> cars, int year, double price) {
        cars.stream()
                .filter(c -> c.getYear() == year && c.getPrice() > price)
                .forEach(System.out::println);
    }

    /**
     * Зберігає список автомобілів у файл.
     */
    // Збереження списку автомобілів у файл
    private static void saveCarsToFile(String filePath, List<Car> cars) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Car c : cars) {
                writer.write(c.getId() + "," + c.getModel() + "," + c.getYear() + "," +
                        c.getPrice() + "," + c.getRegNumber() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Помилка запису у файл: " + e.getMessage());
        }
    }
}
