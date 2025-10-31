package model;

public class Attacker extends Droid {

    public Attacker(String name, int health, int damage, int energy, int superPower) {
        super(name, health, damage, energy, superPower); //передає параметри в конструктор батьківського класу
    }
    public void superStrike(Droid target) {
        if (this.energy > 0) {
            System.out.println(this.name + " виконує суперудар!");
            target.takeDamage(this.damage * 2);
            this.energy--;
        } else {
            System.out.println(this.name + " не має енергії для суперудару!");
        }

    }
    public void useSuperPower(Droid enemy){
        if (superPowerUses > 0) {
            int boostedDamage = this.damage * 2;
            enemy.takeDamage(boostedDamage);
            System.out.println(name + " завдав подвійної шкоди (" + boostedDamage + ")");
            superPowerUses--;
        } else {
            System.out.println(name + " не має суперсил!");
        }
    }

    @Override
    public String toString() {
        return "⚔️ " + name + " — Атакер (HP: " + health + ", DMG: " + damage + ")";
    }

}


