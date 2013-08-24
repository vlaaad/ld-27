package com.vlaaad.tenseconds.world;

import com.badlogic.gdx.utils.ObjectMap;
import com.vlaaad.common.events.EventDispatcher;
import com.vlaaad.common.events.EventType;
import com.vlaaad.common.grid.BoundedGrid2D;

import java.util.Iterator;

/**
 * Created 24.08.13 by vlaaad
 */
public class World implements Iterable<Cell> {
    public static final int SIZE = 3;

    public static final EventType<Cell> CELL_ADDED = new EventType<Cell>();
    public static final EventType<Cell> CELL_REMOVED = new EventType<Cell>();

    public final EventDispatcher dispatcher = new EventDispatcher();

    private final BoundedGrid2D<Cell> grid = new BoundedGrid2D<Cell>(SIZE, SIZE);
    private final ObjectMap<Class, WorldController> controllerMap = new ObjectMap<Class, WorldController>();

    // ------------- grid ------------- //

    public Cell put(int x, int y, Cell value) {
        if (x < 0 || y < 0 || x >= grid.width || y >= grid.height)
            return null;
        if (value == null)
            throw new IllegalArgumentException("cell can't be null");
        Cell prev = grid.put(x, y, value);
        if (prev != null) {
            value.world = null;
            dispatcher.dispatch(CELL_REMOVED, prev);
        }
        value.world = this;
        value.x = x;
        value.y = y;
        dispatcher.dispatch(CELL_ADDED, value);
        return prev;
    }

    public Cell remove(int x, int y) {
        Cell prev = grid.remove(x, y);
        if (prev != null) {
            prev.world = null;
            dispatcher.dispatch(CELL_REMOVED, prev);
        }
        return prev;
    }


    public int getWidth() { return grid.width; }

    public int getHeight() { return grid.height; }

    public Cell get(int x, int y) { return grid.get(x, y); }

    @Override
    public Iterator<Cell> iterator() { return grid.values().iterator(); }

    public int size() { return grid.size(); }

    // ------------- controllers ------------- //

    public void addController(WorldController controller) {
        controllerMap.put(controller.getClass(), controller);
        controller.doInit(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends WorldController> T getController(Class<T> type) {
        WorldController result = controllerMap.get(type);
        if (result == null)
            return null;
        return (T) result;
    }

    public <T extends WorldController> void removeController(Class<T> type) {
        WorldController c = controllerMap.remove(type);
        if(c==null)
            return;
        c.doDestroy();
    }

    // ------------- act ------------- //

    public void act(float delta) {
        for (WorldController controller : controllerMap.values()) {
            controller.doAct(delta);
        }
    }

}