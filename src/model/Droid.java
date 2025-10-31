package model;

public abstract class Droid implements Cloneable {
    public String name;
    public int health;
    public int damage;
    public int energy;
    public int superPowerUses;
    private final int maxHealth;

    public Droid(String name, int health, int damage, int energy, int superPowerUses) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.energy = energy;
        this.superPowerUses = superPowerUses;
        this.maxHealth = health; // зберігаємо початкове здоров'я
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getSuperPowerUses() {
        return superPowerUses;
    }

    public boolean isAlive() { //перевірка чи живий дроїд
        return this.health > 0;
    }

    public void takeDamage(int amount) {
        this.health -= amount; //скільки завдається шкоди
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public abstract void useSuperPower(Droid enemy); //супер сила - у кожного дроїда різна
    //приймає параметром дроїда на якого застосовує суперсилу

    @Override
    public String toString() {
        return name + " (Здоров'я: " + health + ", Шкода: " + damage + ", Енергія: " + energy + ")";
    }

    @Override
    public Droid clone() {
        try {
            return (Droid) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported for Droid.", e);
        }
    }
}
