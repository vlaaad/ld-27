package com.vlaaad.tenseconds.world.bonuses;

import com.vlaaad.common.events.EventType;
import com.vlaaad.common.events.Listener;
import com.vlaaad.tenseconds.world.Bonus;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.World;
import com.vlaaad.tenseconds.world.controllers.ViewController;
import com.vlaaad.tenseconds.world.events.CellEvent;

/**
 * Created 25.08.13 by vlaaad
 */
public class AddGreen extends Bonus {

    private World world;

    @Override
    public String getIconName() {
        return "add-green";
    }

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public void apply(World world) {
        this.world = world;
        world.timeScale = 0;
        world.dispatcher.add(ViewController.ON_TAP, onTap);
        for (Cell cell : world) {
            cell.setEnabled(false);
        }
    }

    private final Listener<CellEvent> onTap = new Listener<CellEvent>() {
        @Override
        public void handle(EventType<CellEvent> type, CellEvent cellEvent) {
            if (cellEvent.cell != null)
                return;
            world.put(cellEvent.x, cellEvent.y, new Cell());
            for (Cell cell : world) {
                cell.setEnabled(true);
                world.timeScale = 1;
            }
            world.dispatcher.remove(ViewController.ON_TAP, onTap);
        }

    };
}
