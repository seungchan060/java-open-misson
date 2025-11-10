package game.core.bench;

import game.core.entity.Unit;

import java.util.LinkedList;
import java.util.List;

public final class Bench {
    private final int capacity;
    private final LinkedList<Unit> slots = new LinkedList<>();

    public Bench(int capacity) { this.capacity = capacity; }

    public boolean isFull() { return slots.size() >= capacity; }

    public List<Unit> slots() { return List.copyOf(slots); }
}