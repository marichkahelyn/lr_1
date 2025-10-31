package model;

public class Defender extends Droid {
    public Defender(String name, int health, int damage, int energy, int superPower) {
        super(name, health, damage, energy, superPower);
    }
    public void shield() {
        if (this.energy > 0){
            System.out.println(this.name + " використовує щит!");
            this.health += 20;
            this.energy--;
        } else {
            System.out.println(this.name + " не має енергії для суперудару!");
        }
    }

    @Override
    public void useSuperPower(Droid enemy) {
        if (superPowerUses > 0) {
            this.energy += 30;
            System.out.println(name + " активував щит (+30 енергії)");
            superPowerUses--;
        } else {
            System.out.println(name + " не має суперсил!");
        }
    }

    @Override
    public String toString() {
        return "🛡️ " + name + " — Захисник (HP: " + health + ", Armor: " + energy + ")";
    }

}

