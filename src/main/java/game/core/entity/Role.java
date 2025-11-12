package game.core.entity;

import game.core.skill.*;
import java.util.List;

public enum Role {

    KNIGHT(new ShieldBash()),
    ROGUE(new Backstab(), new SmokeBomb()),
    ARCHER(new PiercingShot()),
    MAGE(new Fireball()),
    MONK(),
    TANK();

    private final List<Skill> skills;

    Role(Skill... skills) {
        this.skills = List.of(skills);
    }

    public List<Skill> skills() {
        return skills;
    }
}