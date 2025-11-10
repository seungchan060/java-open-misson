package game.core.bench;

import game.core.entity.Unit;

import java.util.LinkedList;
import java.util.List;

public final class Bench {
    private final int capacity;
    private final LinkedList<Unit> slots = new LinkedList<>();

    public Bench(int capacity) { this.capacity = capacity; }

    public boolean add(Unit u) {
        if (slots.size() >= capacity) return false;
        slots.add(u);
        return true;
    }

    public boolean isFull() { return slots.size() >= capacity; }

    public List<Unit> slots() { return List.copyOf(slots); }

    public Unit remove(int index) {
        if (index < 0 || index >= slots.size()) return null;
        return slots.remove(index);
    }
}