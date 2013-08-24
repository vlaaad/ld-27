package com.vlaaad.tenseconds.world.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.World;
import com.vlaaad.tenseconds.world.WorldController;
import com.vlaaad.tenseconds.world.events.CellEvent;
import com.vlaaad.tenseconds.world.view.CellView;

/**
 * Created 24.08.13 by vlaaad
 */
public class ViewController extends WorldController {

    public static final EventType<CellEvent> ON_TAP = new EventType<CellEvent>();

    private static ObjectMap<Cell.Level, Color> colors = new ObjectMap<Cell.Level, Color>();

    static {
        colors.put(Cell.Level.ONE, Color.GREEN);
//        colors.put(Cell.Level.TWO, Color.YELLOW);
        colors.put(Cell.Level.THREE, Color.RED);
    }

    private final Stage stage;
    private final Group root = new Group();
    private float cellSize;
    private ObjectMap<Cell, CellView> viewMap = new ObjectMap<Cell, CellView>();
    private Table table;

    public ViewController(Stage stage) {
        this.stage = stage;
    }

    @Override
    protected void init() {
        cellSize = stage.getWidth() / world.getWidth();
        root.setSize(stage.getWidth(), stage.getWidth());
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(root);
        world.dispatcher.add(World.CELL_ADDED, onCellAdded);
        world.dispatcher.add(World.CELL_REMOVED, onCellRemoved);
        world.dispatcher.add(Cell.LEVEL_CHANGED, onLevelChanged);
        world.dispatcher.add(Cell.ENABLED_STATUS_CHANGED, onEnableStatusChanged);
        root.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int wx = (int) (x / cellSize);
                int wy = (int) (y / cellSize);
                Cell cell = world.get(wx, wy);
                if (cell != null)
                    world.dispatcher.dispatch(ON_TAP, new CellEvent(wx, wy, cell));
            }
        });
    }

//    @Override
//    protected void pause() {
//        root.setTouchable(Touchable.disabled);
//    }
//
//    @Override
//    protected void resume() {
//        root.setTouchable(Touchable.enabled);
//    }

    @Override
    protected void destroy() {
        table.remove();
        world.dispatcher.remove(World.CELL_ADDED, onCellAdded);
        world.dispatcher.remove(World.CELL_REMOVED, onCellRemoved);
        world.dispatcher.remove(Cell.LEVEL_CHANGED, onLevelChanged);
        world.dispatcher.remove(Cell.ENABLED_STATUS_CHANGED, onEnableStatusChanged);
    }


    private final Listener<Cell> onCellAdded = new Listener<Cell>() {
        @Override
        public void handle(EventType<Cell> type, Cell cell) {
            addView(cell.getX(), cell.getY(), cell);
        }
    };

    private final Listener<Cell> onCellRemoved = new Listener<Cell>() {
        @Override
        public void handle(EventType<Cell> type, Cell cell) {
            removeView(cell);
        }
    };

    private final Listener<Cell> onLevelChanged = new Listener<Cell>() {
        @Override
        public void handle(EventType<Cell> type, Cell cell) {
            setColor(cell);
        }
    };

    private final Listener<Cell> onEnableStatusChanged = new Listener<Cell>() {
        @Override
        public void handle(EventType<Cell> type, Cell cell) {
            setStatus(cell);
        }
    };

    private void setStatus(Cell cell) {
        setStatus(viewMap.get(cell), cell.isEnabled());
    }

    private void setStatus(CellView cellView, boolean enabled) {
        cellView.setDisabled(!enabled);
    }

    private void setColor(Cell cell) {
        setColor(viewMap.get(cell), cell.getLevel());
    }

    private void setColor(CellView cellView, Cell.Level level) {
        Color color = colors.get(level);
        cellView.setColor(color);
    }

    private void addView(int x, int y, Cell cell) {
        CellView view = new CellView();
        view.setSize(cellSize, cellSize);
        root.addActor(view);
        view.setPosition(x * cellSize, y * cellSize);
        viewMap.put(cell, view);
        setColor(view, cell.getLevel());
        setStatus(view, cell.isEnabled());
    }

    @Override
    protected void act(float delta) {
        for (Cell cell : world) {
            int time = (int) cell.time;
            CellView view = viewMap.get(cell);
            view.setText(String.valueOf(time));
            float px = cell.getX() * cellSize;
            float py = cell.getY() * cellSize;
            if (time <= 2) {
                view.setPosition(px + MathUtils.random(-1, 1), py + MathUtils.random(-1, 1));
            } else {
                view.setPosition(px, py);
            }
        }
    }


    private void removeView(Cell cell) {
        CellView view = viewMap.remove(cell);
        view.remove();
    }
}
