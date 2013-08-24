package com.vlaaad.tenseconds.world.events;

import com.vlaaad.tenseconds.world.Cell;

/**
 * Created 24.08.13 by vlaaad
 */
public class CellEvent {
    public final int x;
    public final int y;
    public final Cell cell;

    public CellEvent(int x, int y, Cell cell) {
        this.x = x;
        this.y = y;
        this.cell = cell;
    }
}
