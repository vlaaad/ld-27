package com.vlaaad.tenseconds.world.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.ObjectMap;
import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.tenseconds.AppConfig;
import com.vlaaad.tenseconds.world.Bonus;
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
    public static final EventType<Bonus> BONUS_SELECTED = new EventType<Bonus>();

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
    private Label bonusPoints;
    private ObjectMap<ImageTextButton, Bonus> bonusMap = new ObjectMap<ImageTextButton, Bonus>();


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

        root.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int wx = (int) (x / cellSize);
                int wy = (int) (y / cellSize);
                Cell cell = world.get(wx, wy);
//                if (cell != null)
                    world.dispatcher.dispatch(ON_TAP, new CellEvent(wx, wy, cell));
            }
        });

        bonusPoints = new Label("bonus points: 0", AppConfig.skin);
        bonusPoints.setAlignment(Align.center);

        table.add(bonusPoints).colspan(BonusController.bonuses.size).row();
        table.add(root).colspan(BonusController.bonuses.size).row();
        for (Bonus bonus : BonusController.bonuses) {
            ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(AppConfig.skin.get(ImageTextButton.ImageTextButtonStyle.class));
            style.imageUp = AppConfig.skin.getDrawable(bonus.getIconName());
            ImageTextButton button = new ImageTextButton(bonus.getCost() + " bp", style);
            button.addListener(createBonusButtonListener(bonus));
            button.setDisabled(true);
            table.add(button);
            bonusMap.put(button, bonus);
        }

        world.dispatcher.add(World.CELL_ADDED, onCellAdded);
        world.dispatcher.add(World.CELL_REMOVED, onCellRemoved);
        world.dispatcher.add(Cell.LEVEL_CHANGED, onLevelChanged);
        world.dispatcher.add(Cell.ENABLED_STATUS_CHANGED, onEnableStatusChanged);
        world.dispatcher.add(BonusController.BONUS_POINTS_GAINED, onBonusPointsGained);
        world.dispatcher.add(BonusController.BONUS_POINTS_SPENT, onBonusPointsSpent);
    }

    @Override
    protected void destroy() {
        table.remove();
        world.dispatcher.remove(World.CELL_ADDED, onCellAdded);
        world.dispatcher.remove(World.CELL_REMOVED, onCellRemoved);
        world.dispatcher.remove(Cell.LEVEL_CHANGED, onLevelChanged);
        world.dispatcher.remove(Cell.ENABLED_STATUS_CHANGED, onEnableStatusChanged);
        world.dispatcher.remove(BonusController.BONUS_POINTS_GAINED, onBonusPointsGained);
        world.dispatcher.remove(BonusController.BONUS_POINTS_SPENT, onBonusPointsSpent);
    }

    private EventListener createBonusButtonListener(final Bonus bonus) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                world.dispatcher.dispatch(BONUS_SELECTED, bonus);
            }
        };
    }

    private final Listener<Integer> onBonusPointsSpent = new Listener<Integer>() {
        @Override
        public void handle(EventType<Integer> type, Integer integer) {
            bonusPoints.setText("bonus points: " + integer);
        }
    };

    private final Listener<BonusController.GainBonusPointsEvent> onBonusPointsGained = new Listener<BonusController.GainBonusPointsEvent>() {
        @Override
        public void handle(EventType<BonusController.GainBonusPointsEvent> type, final BonusController.GainBonusPointsEvent event) {
            final Label points = new Label("+" + event.value, AppConfig.skin);
            stage.addActor(points);
            final Vector2 tmp = new Vector2(
                    event.x * cellSize + cellSize / 2 - points.getPrefWidth() / 2,
                    event.y * cellSize + cellSize / 2 - points.getPrefHeight() / 2);
            root.localToStageCoordinates(tmp);
            points.setPosition(tmp.x, tmp.y);

            tmp.set(bonusPoints.getPrefWidth() - points.getPrefWidth(), 0);
            bonusPoints.localToStageCoordinates(tmp);

            points.addAction(Actions.sequence(
                    Actions.moveTo(tmp.x, tmp.y, 0.5f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            points.remove();
                            bonusPoints.setText("bonus points: " + event.total);
                        }
                    })
            ));
            for (ImageTextButton button : bonusMap.keys()) {
                Bonus bonus = bonusMap.get(button);
                button.setDisabled(bonus.getCost() > event.total);
            }
        }
    };

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
