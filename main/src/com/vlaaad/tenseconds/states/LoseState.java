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
public class LoseState extends State {

    public static interface Callback {
        public void onGoToMainMenu();

        public void onTryAgain();
    }

    private final Callback callback;

    public LoseState(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void init() {
        Table table = new Table();
        table.setFillParent(true);

        Label label = new Label("You lose!", AppConfig.skin, "default", Color.RED);
        label.setAlignment(Align.center);
        label.setFontScale(2);

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

        TextButton again = new TextButton("try again", AppConfig.skin, "menu");
        again.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onTryAgain();
            }
        });

        table.add(label).row();
        table.add(again).size(60, 20).row();
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
