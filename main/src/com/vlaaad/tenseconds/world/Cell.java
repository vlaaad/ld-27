package com.vlaaad.tenseconds.world;

import com.vlaaad.common.events.EventType;

/**
 * Created 24.08.13 by vlaaad
 */
public class Cell {
    public static enum Level {
        ONE, /*TWO, */THREE
    }

    public static final EventType<Cell> LEVEL_CHANGED = new EventType<Cell>();
    public static final EventType<Cell> ENABLED_STATUS_CHANGED = new EventType<Cell>();

    World world;
    int x;
    int y;

    public int getX() { return x; }

    public int getY() { return y; }

    private Level level = Level.ONE;
    private boolean enabled = true;
    public float time = 10;

    public Level getLevel() { return level; }

    public void setLevel(Level level) {
        if (level == null)
            throw new IllegalArgumentException("level can't be null");
        if (this.level == level)
            return;
        this.level = level;
        if (world != null) {
            world.dispatcher.dispatch(LEVEL_CHANGED, this);
        }
    }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;
        this.enabled = enabled;
        if (world != null) {
            world.dispatcher.dispatch(ENABLED_STATUS_CHANGED, this);
        }
    }


    public World getWorld() { return world; }
}
