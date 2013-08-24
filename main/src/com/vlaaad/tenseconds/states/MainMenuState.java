package com.vlaaad.tenseconds.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.vlaaad.common.State;
import com.vlaaad.tenseconds.AppConfig;

/**
 * Created 24.08.13 by vlaaad
 */
public class MainMenuState extends State {

    public static interface Callback {
        public void onStartNewGame();
    }

    private final Callback callback;

    public MainMenuState(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void init() {
        Table table = new Table();
        table.setFillParent(true);
        TextButton play = new TextButton("play", AppConfig.skin, "menu");
        TextButton exit = new TextButton("exit", AppConfig.skin, "menu");

        table.add(play).size(50);
        table.add(exit).size(50);
        getStage().addActor(table);

        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onStartNewGame();
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
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
