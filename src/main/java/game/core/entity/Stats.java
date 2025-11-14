package game.core.entity;

public final class Stats {
    private int hp;
    private final int maxHp;
    private int atk;
    private int def;
    private int armor;
    private int mana;

    private Stats(int hp, int atk, int def, int armor, int mana) {
        this.hp = hp;
        this.maxHp = hp;
        this.atk = atk;
        this.def = def;
        this.armor = armor;
        this.mana = mana;
    }

    public static Stats of(int hp, int atk, int def, int armor, int mana) {
        return new Stats(hp, atk, def, armor, mana);
    }

    public int hp()   { return hp; }
    public int atk()  { return atk; }
    public int mana() { return mana; }

    public boolean isDead() { return hp <= 0; }

    public boolean hasMana(int cost) {
        return mana >= cost;
    }

    public boolean consumeMana(int cost) {
        if (mana < cost) return false;
        mana -= cost;
        return true;
    }

    public void applyDamage(int dmg) {
        if (dmg <= 0) return;
        int reduced = Math.max(1, dmg - Math.max(0, def));
        hp = Math.max(0, hp - reduced);
    }
}