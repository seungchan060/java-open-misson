package game.core.service;

import game.core.entity.Unit;

import java.util.List;

public final class ContextUnitsHolder {
    private static final ThreadLocal<List<Unit>> TL = new ThreadLocal<>();

    public static void open(List<Unit> units) { TL.set(units); }
    public static void close() { TL.remove(); }
    public static List<Unit> currentUnits() { return TL.get(); }
}