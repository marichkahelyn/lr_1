package util;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;


// Керує збереженням та завантаженням логів бою з файлу.

public class BattleLogManager { //клас для збереження та завантаження логів

    public static void saveLogToFile(Stage owner, List<String> battleLog) {
        if (battleLog == null || battleLog.isEmpty() || battleLog.get(0).contains("не доступний")) {
            showAlert(Alert.AlertType.WARNING, "Помилка збереження", "Лог бою ще не створений або не доступний.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Записати бій у файл");
        fileChooser.setInitialFileName("battle_log.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстові файли (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(owner);// повертає файл який вибрав користувач
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : battleLog) {
                    writer.write(line); //кожен рядок записуєм у файл
                    writer.newLine();
                }
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Лог бою успішно збережено у файл: " + file.getName());
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Помилка збереження", "Помилка при записі файлу: " + ex.getMessage());
            }
        }
    }

    public static List<String> loadLogFromFile(Stage owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Відтворити бій із файлу");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстові файли (*.txt)", "*.txt"));

        File file = fileChooser.showOpenDialog(owner);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                List<String> log = reader.lines().collect(Collectors.toList());
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Лог бою успішно завантажено.");
                return log;
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Помилка завантаження", "Помилка при зчитуванні файлу: " + ex.getMessage());
            }
        }
        return null;
    }

    private static void showAlert(Alert.AlertType type, String title, String message) { //допоміжний метод для показу повідомлень користувачу
        Alert alert = new Alert(type);
        alert.setTitle(title); //заголовок
        alert.setHeaderText(null);
        alert.setContentText(message); //текст повідомлення
        alert.showAndWait();//показуємо вікно користувачу
    }
}


