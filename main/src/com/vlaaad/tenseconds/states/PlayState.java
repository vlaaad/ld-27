package com.vlaaad.tenseconds.states;

import com.badlogic.gdx.graphics.Color;
import com.vlaaad.common.State;
import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.World;
import com.vlaaad.tenseconds.world.controllers.GameController;
import com.vlaaad.tenseconds.world.controllers.RulesController;
import com.vlaaad.tenseconds.world.controllers.ViewController;

/**
 * Created 24.08.13 by vlaaad
 */
public class PlayState extends State implements Listener<GameController.Results> {
    public static interface Callback {
        public void onWin(float totalTime);

        public void onLose(float totalTime);
    }

    private final Callback callback;
    private final World world = new World();

    public PlayState(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void init() {
        world.addController(new ViewController(getStage()));
        world.addController(new RulesController());
        world.addController(new GameController());

        Cell cell = new Cell();

        int p = (int) (World.SIZE / 2f - 0.5f);
        world.put(p, p, cell);

        world.dispatcher.add(GameController.RESULT, this);
    }


    @Override
    public void handle(EventType<GameController.Results> type, GameController.Results results) {
        if (results.win) {
            callback.onWin(results.totalTime);
        } else {
            callback.onLose(results.totalTime);
        }
    }

    @Override
    protected void resume() {
        //TODO implement
    }

    @Override
    protected void resize(float width, float height) {
        //TODO implement
    }

    @Override
    protected void pause() {
        //TODO implement
    }

    @Override
    protected void render(float delta) {
        world.act(delta);
    }

    @Override
    protected void dispose() {
        //TODO implement
    }

    @Override
    protected Color getBackgroundColor() {
        return Color.BLACK;
    }
}
