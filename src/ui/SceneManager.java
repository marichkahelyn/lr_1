package ui;

import game.BattleSimulator;
import game.TeamBattleSimulator;
import javafx.application.Platform;
import javafx.geometry.Insets; //для відступів
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;//вертикальний контейнер
import javafx.stage.Stage;
import model.Droid;
import model.Attacker;
import model.Defender;
import model.Healer;
import util.BattleLogManager;

import java.util.*;
import java.util.function.BiConsumer;

public class SceneManager {
    private final Stage primaryStage; //головна вікно програми
    private final List<Droid> allDroids;
    private List<String> lastBattleLog = List.of("Лог ще не доступний. Проведіть бій.");

    public SceneManager(Stage primaryStage, List<Droid> allDroids) {
        this.primaryStage = primaryStage;
        this.allDroids = allDroids;
    }

    public void showMainMenu() {
        VBox menu = new VBox(15); //створює контейнер, у якому елементи будуть розташовані вертикально з відступом 15 пікселів.
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));

        Label title = new Label("🤖 БИТВА ДРОЇДІВ");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;"); //робить шрифт заголовка великим і жирним

        Button createBtn = new Button(" Створити дроїда");
        Button showBtn = new Button(" Показати дроїдів");
        Button duelBtn = new Button(" Бій 1 на 1");
        Button teamBtn = new Button(" Команда на команду (3х3)");
        Button saveBtn = new Button(" Записати бій у файл");
        Button loadBtn = new Button(" Відтворити бій із файлу");
        Button exitBtn = new Button(" Вихід");

        for (Button b : new Button[]{createBtn, showBtn, duelBtn, teamBtn, saveBtn, loadBtn, exitBtn}) {
            b.setMinWidth(250); //однакова ширина для кнопок
            b.setStyle("-fx-font-size: 16px; -fx-background-color: #1e90ff; -fx-text-fill: white;"); //білий текст , синя кнопка
        }

        createBtn.setOnAction(e -> showCreateDroidForm()); // відкривається вікно для створення дроїда
        showBtn.setOnAction(e -> showDroidList());
        duelBtn.setOnAction(e -> showDroidSelectionForBattle());
        teamBtn.setOnAction(e -> showTeamSelectionForBattle());
        saveBtn.setOnAction(e -> BattleLogManager.saveLogToFile(primaryStage, lastBattleLog)); //запис у файл
        loadBtn.setOnAction(e -> showLoadBattleLog());
        exitBtn.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(title, createBtn, showBtn, duelBtn, teamBtn, saveBtn, loadBtn, exitBtn);
        primaryStage.setScene(new Scene(menu, 800, 600)); //розмір вікна
        primaryStage.setTitle("Меню — 3D Битва дроїдів");
        primaryStage.show();
    }

    // Створення дроїда
    public void showCreateDroidForm() {
        VBox form = new VBox(10); //відступ 10 пікселів між елементами
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        Label title = new Label("🛠 Створення нового дроїда");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Ім'я дроїда");

        TextField healthField = new TextField();
        healthField.setPromptText("Здоров'я");
        TextField damageField = new TextField();
        damageField.setPromptText("Шкода");
        TextField armorField = new TextField();
        armorField.setPromptText("Броня");
        TextField levelField = new TextField();
        levelField.setPromptText("Рівень");

        ComboBox<String> typeBox = new ComboBox<>(); //випадаючий список
        typeBox.getItems().addAll("Attacker", "Defender", "Healer");

        typeBox.setOnAction(e -> {
            String type = typeBox.getValue();
            if (type == null) return;
            switch (type) {
                case "Attacker" -> { healthField.setText("120"); damageField.setText("40"); armorField.setText("10"); levelField.setText("1"); }
                case "Defender" -> { healthField.setText("200"); damageField.setText("20"); armorField.setText("40"); levelField.setText("1"); }
                case "Healer" -> { healthField.setText("150"); damageField.setText("10"); armorField.setText("15"); levelField.setText("1"); }
            }
        });

        Button saveBtn = new Button("💾 Зберегти");
        Button backBtn = new Button("⬅ Назад");
        Label message = new Label();

        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) { message.setText("❌ Введіть ім'я дроїда!"); return; }
                String type = typeBox.getValue();
                if (type == null) { message.setText("❌ Оберіть тип дроїда!"); return; }

                int health = Integer.parseInt(healthField.getText().trim());
                int damage = Integer.parseInt(damageField.getText().trim());
                int armor = Integer.parseInt(armorField.getText().trim());
                int level = Integer.parseInt(levelField.getText().trim());

                Droid newDroid = switch (type) {
                    case "Attacker" -> new Attacker(name, health, damage, armor, level);
                    case "Defender" -> new Defender(name, health, damage, armor, level);
                    case "Healer" -> new Healer(name, health, damage, armor, level);
                    default -> throw new IllegalArgumentException();
                };

                allDroids.add(newDroid);
                message.setText("✅ Дроїда '" + name + "' успішно створено!");
                nameField.clear(); healthField.clear(); damageField.clear(); armorField.clear(); levelField.clear(); typeBox.setValue(null); //очищення після введення
            } catch (Exception ex) { message.setText("❌ Помилка: перевірте введені дані!"); }
        });

        backBtn.setOnAction(e -> showMainMenu()); //кнопка назад
        form.getChildren().addAll(title, nameField, healthField, damageField, armorField, levelField, typeBox, saveBtn, message, backBtn);
        primaryStage.setScene(new Scene(form, 800, 600)); //інша сторінка
    }

    // Список дроїдів
    public void showDroidList() {
        VBox listBox = new VBox(10);
        listBox.setAlignment(Pos.CENTER);
        listBox.setPadding(new Insets(20));
        Label title = new Label(" Усі створені дроїди");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        if (allDroids.isEmpty()) listBox.getChildren().add(new Label(" Немає створених дроїдів."));
        else allDroids.forEach(d -> listBox.getChildren().add(new Label(d.toString())));

        Button backBtn = new Button(" Назад");
        backBtn.setOnAction(e -> showMainMenu());
        listBox.getChildren().add(backBtn);
        primaryStage.setScene(new Scene(listBox, 800, 600));
    }

    // Вибір 1 на 1
    public void showDroidSelectionForBattle() { // вибір дроїдів для бою 1 на 1
        VBox box = new VBox(10); box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        Label title = new Label("🤖 Оберіть 2 дроїдів для бою");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        ComboBox<Droid> d1Box = new ComboBox<>(); //випадаючі списки
        ComboBox<Droid> d2Box = new ComboBox<>();
        d1Box.getItems().addAll(allDroids); d2Box.getItems().addAll(allDroids);
        d1Box.setPromptText("Перший дроїд"); d2Box.setPromptText("Другий дроїд");

        Label message = new Label();
        Button startBtn = new Button("🚀 Почати бій");
        Button backBtn = new Button(" Назад");

        startBtn.setOnAction(e -> {
            Droid d1 = d1Box.getValue(), d2 = d2Box.getValue();
            if (d1 == null || d2 == null || d1.equals(d2)) { message.setText("❌ Оберіть двох різних дроїдів!"); return; }
            showBattleSceneWithDroids(d1, d2); //відкриваємо сцену бою
        });

        backBtn.setOnAction(e -> showMainMenu());
        box.getChildren().addAll(title, d1Box, d2Box, startBtn, message, backBtn);
        primaryStage.setScene(new Scene(box, 800, 600));
    }

    public void showBattleSceneWithDroids(Droid d1, Droid d2) { //відкриває сцену бою
        Battlefield3D battlefield = new Battlefield3D(); //клас, який малює 3D арену, дроїдів, їхні рухи
        Scene battleScene = battlefield.createScene(d1, d2, this::showMainMenu);
        primaryStage.setScene(battleScene);
        primaryStage.setTitle("3D Битва дроїдів 1x1");

        //виклик конструктора BattleSimulator
        BattleSimulator simulator = new BattleSimulator(
                d1,                    // перший дроїд
                d2,                    // другий дроїд
                battlefield::animateAttack, // малює рух і постріл у 3D
                this::handleBattleEnd, //що зробити після завершення бою
                battlefield
        );

        simulator.start(); //запускаємо логіку бою
    }

    // Вибір 3 на 3
    public void showTeamSelectionForBattle() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        Label title = new Label("🤖 Оберіть 6 дроїдів для бою 3 на 3");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ComboBox<Droid> t1d1 = createDroidComboBox("Дроїд 1");
        ComboBox<Droid> t1d2 = createDroidComboBox("Дроїд 2");
        ComboBox<Droid> t1d3 = createDroidComboBox("Дроїд 3");
        ComboBox<Droid> t2d1 = createDroidComboBox("Дроїд 1");
        ComboBox<Droid> t2d2 = createDroidComboBox("Дроїд 2");
        ComboBox<Droid> t2d3 = createDroidComboBox("Дроїд 3");

        List<ComboBox<Droid>> boxes = List.of(t1d1, t1d2, t1d3, t2d1, t2d2, t2d3);
        Label message = new Label();
        Button startBtn = new Button("🚀 Почати командний бій 3 на 3");
        Button backBtn = new Button(" Назад");
        startBtn.setStyle("-fx-background-color: darkgreen; -fx-text-fill: white;");
        backBtn.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        startBtn.setOnAction(e -> { //коли натиснуто старт
            List<Droid> selected = new ArrayList<>();
            Set<Droid> unique = new HashSet<>(); //для перевірки чи всі дроїди різні
            for (ComboBox<Droid> boxC : boxes) {
                Droid d = boxC.getValue();
                if (d == null) { message.setText("❌ Заповніть усі 6 полів!"); return; }
                selected.add(d);
                unique.add(d);
            }
            if (unique.size() < 6) { message.setText("❌ Оберіть шість РІЗНИХ дроїдів!"); return; }
            List<Droid> team1 = selected.subList(0,3);
            List<Droid> team2 = selected.subList(3,6);
            showTeamBattleScene(team1, team2); //сцена командного бою
        });

        backBtn.setOnAction(e -> showMainMenu());

        VBox team1Box = new VBox(5, new Label("Команда 1:"), t1d1, t1d2, t1d3);
        team1Box.setAlignment(Pos.CENTER);
        VBox team2Box = new VBox(5, new Label("Команда 2:"), t2d1, t2d2, t2d3);
        team2Box.setAlignment(Pos.CENTER);

        box.getChildren().addAll(title, team1Box, new Separator(), team2Box, startBtn, message, backBtn);
        primaryStage.setScene(new Scene(box, 800, 600));
    }

    private ComboBox<Droid> createDroidComboBox(String prompt) {
        ComboBox<Droid> box = new ComboBox<>(); box.getItems().addAll(allDroids); box.setPromptText(prompt); box.setMinWidth(250); return box;
    }

    public void showTeamBattleScene(List<Droid> team1, List<Droid> team2) {
        Battlefield3D battlefield = new Battlefield3D();
        Scene battleScene = battlefield.createTeamScene(team1, team2, this::showMainMenu); //3D сцена з двома командами
        primaryStage.setScene(battleScene);
        primaryStage.setTitle("3D Командна Битва 3х3"); //міняємо заголовок вікна

        TeamBattleSimulator simulator = new TeamBattleSimulator(
                team1, team2,
                battlefield::animateAttack,
                this::handleBattleEnd,
                battlefield
        );
        simulator.start();
    }

    private void handleBattleEnd(List<String> battleLog) { //обробляє події після завершення бою
        this.lastBattleLog = battleLog; //зберігає весь лог бою
        String result = battleLog.stream()
                .filter(s -> s.startsWith("🏆") || s.startsWith("🤝") || s.startsWith("⏹"))
                .findFirst().orElse("Бій завершено.");

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, result, ButtonType.OK); //віконне повідомлення
            alert.setTitle("Результат бою");
            alert.setHeaderText("Битва завершена!");
            alert.showAndWait(); //очікує поки користувач натисне ОК
            showMainMenu();
        });
    }

    public void showLoadBattleLog() {
        List<String> loaded = BattleLogManager.loadLogFromFile(primaryStage);
        if (loaded != null) showLogViewer(loaded); //якщо лог успішно завантажено - показує
    }

    private void showLogViewer(List<String> log) {
        VBox box = new VBox(10); //бокс для відображення тексту
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        TextArea area = new TextArea(String.join("\n", log)); //створює текстове поле, вставляє лог і робить тільки для перегляду
        area.setEditable(false);
        area.setPrefSize(750,500);
        Button back = new Button(" Назад");
        back.setOnAction(e -> showMainMenu());
        box.getChildren().addAll(new Label(" Відтворений лог бою"), area, back);
        primaryStage.setScene(new Scene(box, 800,600));
    }
}