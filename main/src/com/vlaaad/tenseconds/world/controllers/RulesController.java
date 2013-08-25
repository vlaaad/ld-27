package com.vlaaad.tenseconds.world.controllers;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.WorldController;
import com.vlaaad.tenseconds.world.events.CellEvent;

/**
 * Created 24.08.13 by vlaaad
 */
public class RulesController extends WorldController {

    public static final EventType<Cell> LOSE = new EventType<Cell>();
    public static final EventType<Void> WIN = new EventType<Void>();

    @Override
    protected void init() {
        world.dispatcher.add(ViewController.ON_TAP, onTap);
    }

    @Override
    protected void destroy() {
        world.dispatcher.remove(ViewController.ON_TAP, onTap);
    }

    private final Listener<CellEvent> onTap = new Listener<CellEvent>() {
        @Override
        public void handle(EventType<CellEvent> type, CellEvent cellEvent) {
            onTap(cellEvent.x, cellEvent.y, cellEvent.cell);
        }
    };

    private void onTap(int x, int y, Cell cell) {
        if (cell == null)
            return;
        if (!cell.isEnabled()) //not used
            return;
        //cell is enabled, can act!
        activate(x, y);
        if (world.get(x, y) != null) {
            cell.time = 10;
        }
        activate(x - 1, y);
        activate(x + 1, y);
        activate(x, y - 1);
        activate(x, y + 1);

        if (world.size() == 0) {
            world.dispatcher.dispatch(WIN, null);
        }
    }

    private void activate(int x, int y) {
        Cell current = world.get(x, y);
        if (current == null) {
            world.put(x, y, new Cell());
        } else {
            int level = current.getLevel().ordinal();
            int totalLevels = Cell.Level.values().length;
            if (level == totalLevels - 1) {
                //max level
                world.remove(x, y);
            } else {
                Cell.Level next = Cell.Level.values()[level + 1];
                current.setLevel(next);
            }
        }
    }

    @Override
    protected void act(float delta) {
        for (Cell other : world) {
            other.time -= delta;
            if (other.time <= 0) {
                world.dispatcher.dispatch(LOSE, other);
                return;
            }
        }
    }
}
