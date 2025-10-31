package game;

import model.Droid;

public class Arena {
    private String name;
    private String effect;

    public Arena(String name, String effect) {
        this.name = name;
        this.effect = effect;
    }

    public void applyEffect(Droid droid) {
        if (this.effect.equals("lower_accuracy")) {
            System.out.println("На арені " + this.name + " точність атак знижена!");
            droid.damage -= 5;
        }
    }
}

