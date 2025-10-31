package ui;

import javafx.animation.ScaleTransition; //анімація масштабування
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.effect.DropShadow; //ефект тіні для об'єму
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D; //базовий тип для 3D фігур
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Attacker;
import model.Defender;
import model.Droid;
import model.Healer;
import javafx.scene.SceneAntialiasing;

import java.util.*;

/**
 * Відповідає за створення та управління 3D-сценою бою.
 */
public class Battlefield3D {

    private Group root3D; //головний контейнер для всіх 3D
    private Camera camera; // камера для точки зору на сцені

    // 3D моделі дроїдів і смуги HP
    private Map<String, Shape3D> droidModels = new HashMap<>();
    private Map<String, Box> hpBars = new HashMap<>();

    // Початкове положення камери
    private static final double CAM_INITIAL_X = 0;
    private static final double CAM_INITIAL_Y = -10;
    private static final double CAM_INITIAL_Z = -40;
    private static final double CAM_INITIAL_ROT = -15;

    public Scene createScene(Droid d1, Droid d2, Runnable onEscape) { //метод для бою 1х1
        return createTeamScene(List.of(d1), List.of(d2), onEscape);
    }

    public Scene createTeamScene(List<Droid> team1, List<Droid> team2, Runnable onEscape) {
        droidModels.clear();
        hpBars.clear();
        root3D = new Group(); //контейнер для всіх 3D

        camera = new PerspectiveCamera(true);
        resetCamera();

        Scene scene3D = new Scene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        scene3D.setFill(Color.SKYBLUE);
        scene3D.setCamera(camera);//прив'язує камеру до сцени

        addArena();
        addLighting();
        addAxisHelpers();

        // команда 1 зліва, команда 2 справа
        for (int i = 0; i < team1.size(); i++) {
            addDroid(team1.get(i), -8, i * 4.0 - 4.0);
        }
        for (int i = 0; i < team2.size(); i++) {
            addDroid(team2.get(i), 8, i * 4.0 - 4.0);
        }

        scene3D.setOnKeyPressed(event -> { //для можливості керування камерою
            switch (event.getCode()) {
                case W -> camera.setTranslateZ(camera.getTranslateZ() + 2.0);
                case S -> camera.setTranslateZ(camera.getTranslateZ() - 2.0);
                case A -> camera.setTranslateX(camera.getTranslateX() - 2.0);
                case D -> camera.setTranslateX(camera.getTranslateX() + 2.0);
                case R -> resetCamera();
                case ESCAPE -> onEscape.run();
                default -> {}
            }
        });

        return scene3D;
    }

    private void resetCamera() {
        if (camera == null) camera = new PerspectiveCamera(true);
        camera.setTranslateX(CAM_INITIAL_X);
        camera.setTranslateY(CAM_INITIAL_Y);
        camera.setTranslateZ(CAM_INITIAL_Z);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(CAM_INITIAL_ROT);
        camera.setFarClip(2000);
    }

    private void addAxisHelpers() {//допоміжні вісі
        Box xAxis = new Box(20, 0.1, 0.1);
        xAxis.setMaterial(new PhongMaterial(Color.RED));

        Box zAxis = new Box(0.1, 0.1, 20);
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));

        root3D.getChildren().addAll(xAxis, zAxis);
    }

    // --- Анімація атаки ---
    public void animateAttack(Droid attacker, Droid target) {
        if (attacker == null || target == null) return;

        Platform.runLater(() -> { //всі дії, що змінюють UI, виконуються в JavaFX-потоці
            Shape3D attackerModel = droidModels.get(attacker.getName()); //атакуючий
            Shape3D targetModel = droidModels.get(target.getName());//ціль

            if (attackerModel == null || targetModel == null) return;

            // поштовх вперед-назад
            TranslateTransition forward = new TranslateTransition(Duration.millis(200), attackerModel);
            forward.setByZ(-1);
            TranslateTransition back = new TranslateTransition(Duration.millis(200), attackerModel);
            back.setByZ(1);

            forward.setOnFinished(e -> {
                back.play();
                spawnProjectile(attackerModel, targetModel, attacker); //рух снаряда
            });
            forward.play(); //старт поштовху
        });
    }

    // --- снаряд між дроїдами ---
    private void spawnProjectile(Shape3D attackerModel, Shape3D targetModel, Droid attacker) {
        Sphere bullet = new Sphere(0.3);
        Color color = switch (attacker) {
            case Attacker a -> Color.ORANGERED;
            case Defender d -> Color.LIGHTBLUE;
            case Healer h -> Color.LIMEGREEN;
            default -> Color.YELLOW;
        };
        bullet.setMaterial(new PhongMaterial(color)); //накладаємо матеріал щоб куля світилась
        bullet.setEffect(new DropShadow(5, color));

        double startX = attackerModel.getTranslateX();
        double startY = attackerModel.getTranslateY() + 0.5;
        double startZ = attackerModel.getTranslateZ();
        bullet.setTranslateX(startX);
        bullet.setTranslateY(startY);
        bullet.setTranslateZ(startZ);
        root3D.getChildren().add(bullet); //додаємо кулю до сцени

        double endX = targetModel.getTranslateX(); //кінцеві позиції
        double endY = targetModel.getTranslateY() + 0.5;
        double endZ = targetModel.getTranslateZ();

        TranslateTransition move = new TranslateTransition(Duration.seconds(0.6), bullet); //анімація руху
        move.setToX(endX);
        move.setToY(endY);
        move.setToZ(endZ);
        move.setOnFinished(ev -> {
            root3D.getChildren().remove(bullet); //видаляємо кулю коли вона попала
            flashTarget(targetModel);
        });
        move.play();//запускаємо анімацію руху кулі
    }

    private void flashTarget(Shape3D targetModel) { //коротке мерехтіння при попадні
        ScaleTransition st = new ScaleTransition(Duration.millis(150), targetModel);
        st.setFromX(1.0);
        st.setToX(0.9);
        st.setCycleCount(2);
        st.setAutoReverse(true);//після виконання автоматично повенути в початковий масштаб
        st.play();
    }

    // --- додавання дроїда + HP-смуги ---
    private void addDroid(Droid droid, double x, double z) {
        Box model = new Box(2, 1, 2); //візуальна модель дроїда
        PhongMaterial material = new PhongMaterial();

        try {
            String texturePath = null;
            if (droid instanceof Attacker) texturePath = "/images/droid1.png";
            else if (droid instanceof Defender) texturePath = "/images/droid2.png";
            else if (droid instanceof Healer) texturePath = "/images/droid3.png";

            if (texturePath != null) { //якщо немає то створюємо вручну
                Image texture = new Image(Objects.requireNonNull(getClass().getResourceAsStream(texturePath)));
                material.setDiffuseMap(texture);
                material.setSpecularColor(Color.LIGHTGRAY);
                material.setSpecularPower(64);
            } else material.setDiffuseColor(Color.DARKGRAY);

        } catch (Exception e) {
            material.setDiffuseColor(Color.GRAY);//якщо не вийшло ставим просто сірий колір
        }

        model.setMaterial(material);
        model.setTranslateX(x);
        model.setTranslateZ(z);
        model.setTranslateY(-1.5);

        root3D.getChildren().add(model); //додаємо до сцени
        droidModels.put(droid.getName(), model); //зберігаємо посилання на модель

        // --- 3D HP-смуга ---
        Box hpBar = new Box(2.0, 0.15, 0.15);
        PhongMaterial hpMat = new PhongMaterial(Color.LIMEGREEN);
        hpBar.setMaterial(hpMat);
        hpBar.setTranslateX(x);
        hpBar.setTranslateY(model.getTranslateY() - 1.0);
        hpBar.setTranslateZ(z); //згідно позиції дроїда

        root3D.getChildren().add(hpBar);
        hpBars.put(droid.getName(), hpBar); //зберігаємо в мапі для можливості змін
    }

    // --- оновлення HP-смуги ---
    public void updateHealth(Droid droid) {
        Platform.runLater(() -> {
            Box hpBar = hpBars.get(droid.getName());//берем відповідного дроїда
            if (hpBar == null) return;

            double ratio = Math.max(0, Math.min(1.0,
                    (double) droid.getHealth() / droid.getMaxHealth())); //відношення поточного до максимального

            // анімація скорочення
            ScaleTransition scale = new ScaleTransition(Duration.millis(300), hpBar);
            scale.setToX(ratio);//масштаб смуги по х
            scale.play();//запуск анімації

            Color color = (ratio > 0.6) ? Color.LIMEGREEN :
                    (ratio > 0.3) ? Color.ORANGE : Color.RED;
            ((PhongMaterial) hpBar.getMaterial()).setDiffuseColor(color);//змінює колір на обчислений
        });
    }

    // --- арена ---
    private void addArena() {
        Box arena = new Box(60, 1, 60);
        arena.setTranslateY(0); //центральна висота
        arena.setMaterial(new PhongMaterial(Color.DARKSLATEGRAY));//колір підлоги
        root3D.getChildren().add(arena);
    }

    // --- освітлення ---
    private void addLighting() {
        AmbientLight ambient = new AmbientLight(Color.rgb(120, 120, 120)); //розсіяне світло
        PointLight point = new PointLight(Color.WHITE);
        point.setTranslateY(-10);
        point.setTranslateZ(-20);// щоб світло було під кутом
        root3D.getChildren().addAll(ambient, point);
    }
}
