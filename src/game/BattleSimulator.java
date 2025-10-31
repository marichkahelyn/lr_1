package game;

import javafx.application.Platform;
import model.Droid;
import ui.Battlefield3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer; //інтерфейси для передачі дій
import java.util.function.Consumer;

/**
 * Симулятор бою 1 на 1.
 */
public class BattleSimulator extends Thread {
    private final Droid d1;
    private final Droid d2;
    private final BiConsumer<Droid, Droid> onAttack; //викликається при кожній атаці
    private final Consumer<List<String>> onBattleEnd; // лог бою(викликається вкінці)
    private final Battlefield3D battlefield; // посилання на сцену щоб оновлювати HP смуги
    private final List<String> battleLog = new ArrayList<>();
    private final Random random = new Random();

    public BattleSimulator(Droid d1, Droid d2,
                           BiConsumer<Droid, Droid> onAttack,
                           Consumer<List<String>> onBattleEnd,
                           Battlefield3D battlefield) {
        this.d1 = d1.clone();
        this.d2 = d2.clone();
        this.onAttack = onAttack;
        this.onBattleEnd = onBattleEnd;
        this.battlefield = battlefield;
    }

    @Override
    public void run() {
        log("🤖 Початок бою між " + d1.getName() + " і " + d2.getName());
        try {
            while (d1.isAlive() && d2.isAlive()) {
                Droid attacker = random.nextBoolean() ? d1 : d2;
                Droid target = (attacker == d1) ? d2 : d1;

                // Виклик анімації атаки
                Platform.runLater(() -> onAttack.accept(attacker, target));
                Thread.sleep(700); //затримка між атакаю і нанесенням шкоди

                int damage = attacker.getDamage(); //сила удару
                target.takeDamage(damage);

                // Оновлення HP через UI потік
                Platform.runLater(() -> battlefield.updateHealth(target));

                log(attacker.getName() + " атакує " + target.getName() +
                        " на " + damage + " шкоди. HP " + target.getName() + ": " + target.getHealth());
                Thread.sleep(1000);
            }

            String result = d1.isAlive() && !d2.isAlive() ? "🏆 Переміг " + d1.getName()
                    : (!d1.isAlive() && d2.isAlive() ? "🏆 Переміг " + d2.getName() : "🤝 Нічия!");

            log(result);
            Platform.runLater(() -> onBattleEnd.accept(battleLog)); //передаємо весь список подій бою

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void log(String message) { //додаємо рядок в лог і одночасно виводимо
        System.out.println(message);
        battleLog.add(message);
    }
}