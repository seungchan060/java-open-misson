package game.core.entity;

public final class Stats {
    private final int maxHp;
    private int hp;
    private final int atk;
    private final int def;
    private final int crit;
    private int mana;

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

    // 방어력을 고려해 피해를 적용 (최소 1 피해)
    public void applyDamage(int rawDamage) {
        int dmg = Math.max(1, rawDamage - def);
        hp = Math.max(0, hp - dmg);
    }

    public boolean isDead() { return hp == 0; }

    // MP가 cost 이상인지 확인
    public boolean hasMana(int cost) {
        return mana >= cost;
    }

    // MP 소비
    public boolean consumeMana(int cost) {
        if (!hasMana(cost)) return false;
        mana -= cost;
        return true;
    }

    // MP 회복이 필요해지면 사용
    public void gainMana(int amount) {
        mana = Math.max(0, mana + amount);
    }
}