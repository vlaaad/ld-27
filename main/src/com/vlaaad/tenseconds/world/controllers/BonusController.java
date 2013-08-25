package com.vlaaad.tenseconds.world.controllers;

import com.badlogic.gdx.utils.Array;
import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.tenseconds.world.Bonus;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.World;
import com.vlaaad.tenseconds.world.WorldController;
import com.vlaaad.tenseconds.world.bonuses.AddGreen;
import com.vlaaad.tenseconds.world.bonuses.AddTime;

/**
 * Created 25.08.13 by vlaaad
 */
public class BonusController extends WorldController {

    public static class GainBonusPointsEvent {
        public final int x;
        public final int y;
        public final int value;
        public final int total;

        public GainBonusPointsEvent(int x, int y, int value, int total) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.total = total;
        }
    }

    public static final Array<Bonus> bonuses = new Array<Bonus>();

    static {
        bonuses.add(new AddTime());
        bonuses.add(new AddGreen());
    }


    public static final EventType<Integer> BONUS_POINTS_SPENT = new EventType<Integer>();
    public static final EventType<GainBonusPointsEvent> BONUS_POINTS_GAINED = new EventType<GainBonusPointsEvent>();

    private int bonusPoints = 0;

    @Override
    protected void init() {
        world.dispatcher.add(World.CELL_REMOVED, onCellRemoved);
        world.dispatcher.add(ViewController.BONUS_SELECTED, onBonusSelected);
    }

    @Override
    protected void destroy() {
        world.dispatcher.remove(World.CELL_REMOVED, onCellRemoved);
        world.dispatcher.remove(ViewController.BONUS_SELECTED, onBonusSelected);
    }

    private final Listener<Bonus> onBonusSelected = new Listener<Bonus>() {
        @Override
        public void handle(EventType<Bonus> type, Bonus bonus) {
            activate(bonus);
        }
    };

    private void activate(Bonus bonus) {
        if (bonus.getCost() > bonusPoints)
            return;
        bonusPoints -= bonus.getCost();
        world.dispatcher.dispatch(BONUS_POINTS_SPENT, bonusPoints);
        bonus.apply(world);
    }

    private final Listener<Cell> onCellRemoved = new Listener<Cell>() {
        @Override
        public void handle(EventType<Cell> type, Cell cell) {
            addPoints(10 - (int) cell.time, cell.getX(), cell.getY());
        }
    };

    private void addPoints(int value, int x, int y) {
        bonusPoints += value;
        world.dispatcher.dispatch(BONUS_POINTS_GAINED, new GainBonusPointsEvent(x, y, value, bonusPoints));
    }
}
