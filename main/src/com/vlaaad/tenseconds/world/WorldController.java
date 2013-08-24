package com.vlaaad.tenseconds.world;

/**
 * Created 24.08.13 by vlaaad
 */
public abstract class WorldController {
    protected World world;

    void doInit(World world) {
        this.world = world;
        init();
        doResume();
    }

    void doAct(float delta) {
        act(delta);
    }

    protected abstract void init();

    protected void act(float delta) {
    }

    void doDestroy() {
        destroy();
        world = null;
    }

    protected abstract void destroy();

    void doPause() {
        pause();
    }

    void doResume() {
        resume();
    }

    protected void resume() {
    }

    protected void pause() {
    }
}
