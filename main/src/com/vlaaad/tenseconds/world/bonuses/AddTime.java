package com.vlaaad.tenseconds.world.bonuses;

import com.vlaaad.tenseconds.world.Bonus;
import com.vlaaad.tenseconds.world.Cell;
import com.vlaaad.tenseconds.world.World;

/**
 * Created 25.08.13 by vlaaad
 */
public class AddTime extends Bonus {
    @Override
    public String getIconName() {
        return "add-time";
    }

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public void apply(World world) {
        for (Cell cell : world) {
            cell.time = 10;
        }
    }
}
