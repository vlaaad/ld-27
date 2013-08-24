package com.vlaaad.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created 14.06.13 by vlaaad
 */
public abstract class State {
    private boolean initialized = false;
    private boolean shown = false;
    private Stage stage;

    final void doResume() {
        if (!initialized) {
            initialized = true;
            stage = new Stage(ScreenConfig.width, ScreenConfig.height, true);
            init();
        }
        if (!shown) {
            shown = true;
            Gdx.input.setInputProcessor(stage);
            resume();
        }
    }

    final void doRender(float delta) {
        Color color = getBackgroundColor();
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        render(delta);
    }

    final void doPause() {
        if (shown) {
            shown = false;
            pause();
            if (disposeOnSwitch()) {
                dispose();
            }
        }
    }

    final void doDispose() {
        if (!initialized)
            return;
        if (shown) {
            shown = false;
            pause();
        }

        dispose();
    }

    final void doResize(){
        resize(ScreenConfig.width, ScreenConfig.height);
    }

    /**
     * Called before first resume
     */
    protected abstract void init();

    /**
     * @see com.badlogic.gdx.ApplicationListener#resume()
     */
    protected abstract void resume();

    /**
     *
     * @param width screen size
     * @param height screen size
     */
    protected abstract void resize(float width, float height);
    /**
     * Called when the state should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    protected void render(float delta) {
    }

    /**
     * @see com.badlogic.gdx.ApplicationListener#pause()
     */
    protected abstract void pause();

    /**
     * Called when this state is no longer the current state for a {@link com.vlaaad.common.App}.
     */
    protected abstract void dispose();

    protected boolean disposeOnSwitch() {
        return false;
    }

    protected Color getBackgroundColor() {
        return Color.GRAY;
    }

    protected final Stage getStage() {
        return stage;
    }

}
