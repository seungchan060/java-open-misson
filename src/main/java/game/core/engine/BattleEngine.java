package game.core.engine;

import game.cli.InputView;
import game.cli.OutputView;
import game.core.board.*;
import game.core.entity.*;
import game.core.rules.FatigueRule;
import game.core.rules.VictoryRule;
import game.core.service.*;

import java.util.List;

public final class BattleEngine {
    private final Board board;
    private final List<Unit> units;
    private final OutputView view;
    private final InputView input;
    private final MovementService movement;
    private final AttackService attack;
    private final EnemyAiService enemyAi;
    private final VictoryRule victory;
    private final FatigueRule fatigue;

    public BattleEngine(Board board, List<Unit> units, OutputView view, InputView input) {
        this.board = board; this.units = units; this.view = view; this.input = input;
        this.movement = new MovementService();
        this.attack = new AttackService();
        this.enemyAi = new EnemyAiService(movement, attack);
        this.victory = new VictoryRule(20); // 턴 한도 20
        this.fatigue = new FatigueRule();
    }
}