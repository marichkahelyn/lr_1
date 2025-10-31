package game;

import model.Droid;

public class Battle {

    public static String simulateBattle(Droid d1, Droid d2) {
        StringBuilder log = new StringBuilder(); //змінна для накопичення тексту
        log.append(" Початок бою: ")
                .append(d1.getName()).append(" VS ").append(d2.getName())
                .append("\n----------------------------------\n");

        int round = 1;

        // Копії, щоб не змінювати початкові стани дроїдів
        Droid fighter1 = d1;
        Droid fighter2 = d2;

        // Симуляція по раундах
        while (fighter1.isAlive() && fighter2.isAlive()) {
            log.append("⚔️ Раунд ").append(round).append(":\n");

            // Хід першого дроїда
            fighter2.takeDamage(fighter1.getDamage());
            log.append(fighter1.getName()).append(" атакує → ")
                    .append(fighter2.getName()).append(" (-")
                    .append(fighter1.getDamage()).append(" HP)\n");

            // Якщо другий дроїд ще живий — атакує у відповідь
            if (fighter2.isAlive()) {
                fighter1.takeDamage(fighter2.getDamage());
                log.append(fighter2.getName()).append(" атакує у відповідь → ")
                        .append(fighter1.getName()).append(" (-")
                        .append(fighter2.getDamage()).append(" HP)\n");
            }

            // Використання суперсили (раз на 3 раунди)
            if (round % 3 == 0 && fighter1.isAlive() && fighter2.isAlive()) {
                log.append("🌟 ").append(fighter1.getName()).append(" активує суперсилу!\n");
                fighter1.useSuperPower(fighter2);
            }

            if (round % 4 == 0 && fighter1.isAlive() && fighter2.isAlive()) {
                log.append("🌟 ").append(fighter2.getName()).append(" активує суперсилу!\n");
                fighter2.useSuperPower(fighter1);
            }

            log.append("💙 ").append(fighter1.getName()).append(" HP: ").append(fighter1.getHealth()).append("\n");
            log.append("❤️ ").append(fighter2.getName()).append(" HP: ").append(fighter2.getHealth()).append("\n");
            log.append("----------------------------------\n");

            round++;
            if (round > 50) { // запобігаємо нескінченному циклу
                log.append(" Бій зупинено — надто довго триває!\n");
                break;
            }
        }

        // Результат
        if (fighter1.isAlive() && !fighter2.isAlive()) {
            log.append("🏆 Переможець: ").append(fighter1.getName()).append("\n");
        } else if (!fighter1.isAlive() && fighter2.isAlive()) {
            log.append( "🏆 Переможець: ").append(fighter2.getName()).append("\n");
        } else {
            log.append("🤝 Нічия!\n");
        }

        return log.toString();
    }
}

