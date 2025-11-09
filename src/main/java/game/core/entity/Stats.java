package game.core.entity;

public final class Stats {
    private final int maxHp;
    private int hp;
    private final int atk;
    private final int def;
    private final int crit;
    private final int mana;

    private Stats(int maxHp, int hp, int atk, int def, int crit, int mana) {
        if (maxHp <= 0 || hp <= 0) throw new IllegalArgumentException("HP는 양수여야 합니다.");
        if (hp > maxHp) throw new IllegalArgumentException("hp는 maxHp 이하여야 합니다.");
        if (crit < 0 || crit > 100) throw new IllegalArgumentException("crit 0..100");
        this.maxHp = maxHp; this.hp = hp; this.atk = atk; this.def = def; this.crit = crit; this.mana = mana;
    }

    public static Stats of(int maxHp, int atk, int def, int crit, int mana) {
        return new Stats(maxHp, maxHp, atk, def, crit, mana);
    }

    public int maxHp() { return maxHp; }
    public int hp() { return hp; }
    public int atk() { return atk; }
    public int def() { return def; }
    public int crit() { return crit; }
    public int mana() { return mana; }
}
