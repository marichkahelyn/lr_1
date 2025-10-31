package game;

import javafx.application.Platform;
import model.Droid;
import ui.Battlefield3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * TeamBattleSimulator: симуляція командного бою (3x3).
 */
public class TeamBattleSimulator extends Thread {
    private final List<Droid> team1; //посилання на список не зміниться після ініціалізації
    private final List<Droid> team2;
    private final List<String> battleLog = new ArrayList<>();
    private final Random random = new Random();

    private final BiConsumer<Droid, Droid> onAttack;
    private final Consumer<List<String>> onBattleEnd;
    private final Battlefield3D battlefield;

    // Конструктор із Battlefield3D
    public TeamBattleSimulator(List<Droid> team1, List<Droid> team2,
                               BiConsumer<Droid, Droid> onAttack,
                               Consumer<List<String>> onBattleEnd,
                               Battlefield3D battlefield) {
        this.team1 = team1.stream().map(Droid::clone).collect(Collectors.toList());
        this.team2 = team2.stream().map(Droid::clone).collect(Collectors.toList());
        this.onAttack = onAttack;
        this.onBattleEnd = onBattleEnd;
        this.battlefield = battlefield; // зберігаємо посилання на 3D сцену
    }

    @Override
    public void run() {
        try {
            log("🤖 Початок командного бою! (" + team1.size() + "х" + team2.size() + ")");
            log("Команда 1: " + getTeamNames(team1));
            log("Команда 2: " + getTeamNames(team2));
            log("----------------------------------------");

            int turn = 0;
            while (isTeamAlive(team1) && isTeamAlive(team2) && turn < 150) {
                Droid attacker;
                List<Droid> defenderTeam;

                // чергування команд
                if (turn % 2 == 0) {
                    attacker = getNextAliveDroid(team1, turn / 2);
                    defenderTeam = team2;
                } else {
                    attacker = getNextAliveDroid(team2, turn / 2);
                    defenderTeam = team1;
                }

                if (attacker != null) {
                    Droid target = getRandomAliveTarget(defenderTeam); //вибираємо випадкову живу ціль з супротивників
                    if (target != null) {
                        log("\n⚔️ Хід " + (turn + 1) + ": " + attacker.getName() + " атакує " + target.getName());
                        performAttack(attacker, target); //робить анімацію і завдає шкоду
                        if (turn % 4 == 0) {
                            performSuperPower(attacker, target);
                        }
                        Thread.sleep(1000);
                    }
                }
                turn++;
            }

            // результат
            String result;
            if (isTeamAlive(team1) && !isTeamAlive(team2)) result = "🏆 Перемогла Команда 1!";
            else if (!isTeamAlive(team1) && isTeamAlive(team2)) result = "🏆 Перемогла Команда 2!";
            else if (!isTeamAlive(team1) && !isTeamAlive(team2)) result = "🤝 Нічия!";
            else result = "⏹ Бій зупинено після 150 раундів!";

            log("\n" + result);
            Platform.runLater(() -> onBattleEnd.accept(battleLog));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void performAttack(Droid attacker, Droid target) throws InterruptedException { //атака одного дроїда на іншого
        Platform.runLater(() -> onAttack.accept(attacker, target));
        Thread.sleep(600);

        int damage = attacker.getDamage();
        target.takeDamage(damage);

        // оновлюємо HP через посилання на battlefield
        Platform.runLater(() -> battlefield.updateHealth(target)); //оновлюємо HP на сцені (змінюємо відсоток заповнення смужки)

        log("  " + target.getName() + " отримує " + damage + " шкоди. Залишилось HP: " + target.getHealth());
    }

    private void performSuperPower(Droid user, Droid target) { //активація супер сили
        if (!user.isAlive() || !target.isAlive() || user.superPowerUses <= 0) return;

        log("🌟 " + user.getName() + " активує суперсилу!");
        user.useSuperPower(target);
        user.superPowerUses--;
    }

    private boolean isTeamAlive(List<Droid> team) { //чи є живий дроїд у команді
        return team.stream().anyMatch(Droid::isAlive);
    }

    private Droid getNextAliveDroid(List<Droid> team, int index) {
        List<Droid> alive = team.stream().filter(Droid::isAlive).collect(Collectors.toList()); //створюємо список живих дроїдів
        if (alive.isEmpty()) return null;
        return alive.get(index % alive.size());
    }

    private Droid getRandomAliveTarget(List<Droid> team) { //випадкова жива ціль з вказаної команди
        List<Droid> aliveTargets = team.stream().filter(Droid::isAlive).collect(Collectors.toList());
        if (aliveTargets.isEmpty()) return null;
        return aliveTargets.get(random.nextInt(aliveTargets.size()));
    }

    private void log(String message) { //друк повідомлень в консоль
        System.out.println(message);
        battleLog.add(message);
    }

    private String getTeamNames(List<Droid> team) { //формуємо рядок з імен дроїдів
        return team.stream().map(Droid::getName).collect(Collectors.joining(", "));
    }
}
