package com.vlaaad.tenseconds.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vlaaad.common.State;
import com.vlaaad.tenseconds.AppConfig;

/**
 * Created 24.08.13 by vlaaad
 */
public class InitState extends State {

    private boolean loaded;

    private final Callback callback;
    private Texture logoTex;
    private Table t;

    public InitState(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void init() {
        logoTex = new Texture("logo.png");
        t = new Table();
        t.setFillParent(true);
        t.add(new Image(new TextureRegion(logoTex)));
        getStage().addActor(t);

        AppConfig.assetManager.load("ui.json", Skin.class);
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
        if (loaded)
            return;
        loaded = AppConfig.assetManager.update();
        if (loaded) {
            AppConfig.skin = AppConfig.assetManager.get("ui.json");
            t.addAction(Actions.sequence(
                    Actions.fadeOut(0.5f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            callback.onInitialized();
                        }
                    })
            ));
        }
    }

    @Override
    protected void dispose() {
        logoTex.dispose();
    }

    @Override
    protected Color getBackgroundColor() {
        return Color.LIGHT_GRAY;
    }

    public static interface Callback {
        public void onInitialized();
    }

    @Override
    protected boolean disposeOnSwitch() {
        return true;
    }
}
