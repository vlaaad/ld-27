package com.vlaaad.common;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created 14.06.13 by vlaaad
 */
public abstract class App implements ApplicationListener {

    private State state;

    protected App() {
    }

    @Override
    public final void create() {
        float density = getDensity(Gdx.graphics.getDensity());
        ScreenConfig.width = (int) (Gdx.graphics.getWidth() / (density * getScreenScale()));
        ScreenConfig.height = (int) (Gdx.graphics.getHeight() / (density * getScreenScale()));

        onCreate();
    }

    public float getDensity(float real) {
        float snap = 0.5f;
        int snaps = Math.round(real / snap);
        return snaps * snap;
    }

    protected float getScreenScale() {
        return 1;
    }

    protected abstract void onCreate();

    protected abstract void onDispose();

    @Override
    public final void dispose() {
        if (state != null) state.doDispose();
        onDispose();
    }

    @Override
    public final void pause() {
        if (state != null) state.doPause();
    }

    @Override
    public final void resume() {
        if (state != null) state.doResume();
    }

    @Override
    public final void render() {
        if (state != null) state.doRender(Gdx.graphics.getDeltaTime());
    }

    @Override
    public final void resize(int width, int height) {
        if (state != null) state.doResize();
    }

    /**
     * Sets the current state. {@link State#pause()} is called on any old state, and {@link State#resume()} is called on the new
     * state, if any.
     */
    public final void setState(State state) {
        if (this.state != null) {
            this.state.doPause();
        }
        this.state = state;
        if (this.state != null) {
            this.state.doResume();
            this.state.doResize();
        }
    }

    /**
     * @return the currently active {@link State}.
     */
    public final State getState() {
        return state;
    }
}
