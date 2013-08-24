package com.vlaaad.tenseconds.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.vlaaad.common.State;
import com.vlaaad.tenseconds.AppConfig;

/**
 * Created 24.08.13 by vlaaad
 */
public class WinState extends State {
    public static interface Callback {
        public void onGoToMainMenu();

        public void onContinue();
    }

    private final Callback callback;
    private final float totalTime;

    public WinState(Callback callback, float totalTime) {
        this.callback = callback;
        this.totalTime = totalTime;
    }

    @Override
    protected void init() {
        Table table = new Table();
        table.setFillParent(true);

        Label label = new Label("You win!\nYour result is:\n" + (int) totalTime + " seconds!", AppConfig.skin, "default", Color.GREEN);
        label.setAlignment(Align.center);
        label.setFontScale(2);

        TextButton next = new TextButton("continue", AppConfig.skin, "menu");
        next.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onContinue();
            }
        });
        TextButton menu = new TextButton("main menu", AppConfig.skin, "menu");
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onGoToMainMenu();
            }
        });
        TextButton exit = new TextButton("exit", AppConfig.skin, "menu");
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        table.add(label).row();
        table.add(next).size(60, 20).row();
        table.add(menu).size(60, 20).row();
        table.add(exit).size(60, 20).row();


        getStage().addActor(table);

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
    protected void dispose() {
        //TODO implement
    }
}
