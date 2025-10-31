package ui;

import javafx.application.Application; //для віконної програми
import javafx.stage.Stage;
import model.Droid;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private List<Droid> allDroids = new ArrayList<>(); //список усіх створених дроїдів
    private SceneManager sceneManager;

    @Override
    public void start(Stage stage) {

        //  Ініціалізація керівника
        this.sceneManager = new SceneManager(stage, allDroids); //передаємо параметрами головне вікно щоб показувати сцени і дроїдів

        //  Запуск першої сцени
        sceneManager.showMainMenu();
    }

    public static void main(String[] args) {
        launch(args); //запускає JavaFX
    }

}

