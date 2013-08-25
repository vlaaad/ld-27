package com.vlaaad.tenseconds.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.vlaaad.common.State;
import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.tenseconds.AppConfig;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.World;
import com.vlaaad.tenseconds.world.controllers.BonusController;
import com.vlaaad.tenseconds.world.controllers.GameController;
import com.vlaaad.tenseconds.world.controllers.RulesController;
import com.vlaaad.tenseconds.world.controllers.ViewController;

/**
 * Created 24.08.13 by vlaaad
 */
public class PlayState extends State implements Listener<GameController.Results> {
    private final Dialog pauseDialog;
    private boolean ended;

    public static interface Callback {
        public void onWin(int width, float totalTime);

        public void onLose(int width, float totalTime);

        public void onCancel();
    }

    private final Callback callback;
    private final World world;

    public PlayState(Callback callback, int sides) {
        this.callback = callback;
        world = new World(sides);

        pauseDialog = new Dialog("pause", AppConfig.skin) {
            @Override
            protected void result(Object object) {
                if (object.equals("continue")) {
                    resumeGame();
                } else if (object.equals("menu")) {
                    PlayState.this.callback.onCancel();
                } else if (object.equals("exit")) {
                    Gdx.app.exit();
                }
            }
        };

        TextButton continueGame = new TextButton("continue", AppConfig.skin, "menu");
        pauseDialog.button(continueGame, "continue").getButtonTable().row();
        pauseDialog.getButtonTable().getCell(continueGame).size(60, 20);

        TextButton menu = new TextButton("menu", AppConfig.skin, "menu");
        pauseDialog.button(menu, "menu").getButtonTable().row();
        pauseDialog.getButtonTable().getCell(menu).size(60, 20);

        TextButton exit = new TextButton("exit", AppConfig.skin, "menu");
        pauseDialog.button(exit, "exit");
        pauseDialog.getButtonTable().getCell(exit).size(60, 20);
        pauseDialog.pad(5);
    }

    @Override
    protected void init() {
        world.addController(new ViewController(getStage()));
        world.addController(new RulesController());
        world.addController(new GameController());
        world.addController(new BonusController());

        Cell cell = new Cell();

        int p = (int) (world.getWidth() / 2f - 0.5f);
        world.put(p, p, cell);

        world.dispatcher.add(GameController.RESULT, this);
    }


    @Override
    public void handle(EventType<GameController.Results> type, GameController.Results results) {
        ended = true;
        if (results.win) {
            callback.onWin(world.getWidth(), results.totalTime);
        } else {
            callback.onLose(world.getWidth(), results.totalTime);
        }
    }

    @Override
    protected void resume() {
        resumeGame();
    }

    @Override
    protected void resize(float width, float height) {
        //TODO implement
    }

    @Override
    protected void pause() {
        if (!ended)
            pauseGame();
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

    @Override
    protected void onBackPressed() {
        if (!world.isPaused())
            pauseGame();
        else {
            resumeGame();
        }
    }

    private void resumeGame() {
        if (!world.isPaused())
            return;
        world.resume();
        pauseDialog.hide();
    }

    private void pauseGame() {
        if (world.isPaused())
            return;
        world.pause();
        pauseDialog.show(getStage());
    }
}
