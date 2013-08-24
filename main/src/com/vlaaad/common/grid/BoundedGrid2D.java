package com.vlaaad.common.grid;

import java.util.Collection;
import java.util.Set;

/**
 * Created 24.08.13 by vlaaad
 */
public class BoundedGrid2D<T> extends Grid2D<T> {
    public final int width;
    public final int height;

    public BoundedGrid2D(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public T put(int x, int y, T value) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            throw new IndexOutOfBoundsException();
        return super.put(x, y, value);
    }
}
