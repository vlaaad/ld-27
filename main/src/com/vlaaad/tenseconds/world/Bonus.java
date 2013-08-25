package com.vlaaad.tenseconds.world;

/**
 * Created 25.08.13 by vlaaad
 */
public abstract class Bonus {
    public abstract String getIconName();
    public abstract int getCost();
    public abstract void apply(World world);
}
