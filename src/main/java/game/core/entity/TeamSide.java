package game.core.entity;

public enum TeamSide {
    PLAYER, ENEMY;

    public TeamSide opponent() {
        return this == PLAYER ? ENEMY : PLAYER;
    }
}