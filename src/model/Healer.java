package model;

import java.util.List;

public class Healer extends Droid {

    public Healer(String name, int health, int damage, int energy, int superPower) {
        super(name, health, damage, energy, superPower);
    }

    public void teamHeal(List<Droid> teammates) {
        if (this.energy > 0) {
            System.out.println(this.name + " лікує команду!");
            for (Droid teammate : teammates) {
                teammate.health += 10;
            }
            this.energy--;
        } else {
            System.out.println(this.name + " не має енергії для лікування!");
        }
    }

    @Override
    public void useSuperPower(Droid enemy) {
        if (superPowerUses > 0) {
            this.health += 40;
            System.out.println(name + " зцілив себе на 40 HP");
            superPowerUses--;
        } else {
            System.out.println(name + " не має суперсил!");
        }
    }

    @Override
    public String toString() {
        return "💚 " + name + " — Лікар (HP: " + health + ", Energy: " + energy + ")";
    }
}