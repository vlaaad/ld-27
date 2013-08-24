package com.vlaaad.tenseconds.world.controllers;

import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.common.logging.Logger;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.WorldController;

/**
 * Created 24.08.13 by vlaaad
 */
public class GameController extends WorldController {
    public static final EventType<Results> RESULT = new EventType<Results>();
    private float time;

    @Override
    protected void init() {
        world.dispatcher.add(RulesController.LOSE, onLose);
        world.dispatcher.add(RulesController.WIN, onWin);
    }

    @Override
    protected void destroy() {
        world.dispatcher.remove(RulesController.LOSE, onLose);
        world.dispatcher.remove(RulesController.WIN, onWin);
    }

    private final Listener<Cell> onLose = new Listener<Cell>() {
        @Override
        public void handle(EventType<Cell> type, Cell cell) {
            results(false);
        }
    };

    private final Listener<Void> onWin = new Listener<Void>() {
        @Override
        public void handle(EventType<Void> type, Void aVoid) {
            results(true);
        }
    };

    private void results(boolean win) {
        world.dispatcher.dispatch(GameController.RESULT, new Results(win, time));
    }

    @Override
    protected void act(float delta) {
        time += delta;
    }

    public static class Results {
        public final boolean win;
        public final float totalTime;

        public Results(boolean win, float totalTime) {
            this.win = win;
            this.totalTime = totalTime;
        }
    }
}
