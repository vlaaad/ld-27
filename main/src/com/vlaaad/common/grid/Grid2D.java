package com.vlaaad.common.grid;

import com.badlogic.gdx.utils.Pool;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created 03.08.13 by vlaaad
 */
public class Grid2D<T> {

    private static Pool<Coordinate> pool = new Pool<Coordinate>() {
        @Override
        protected Coordinate newObject() {
            return new Coordinate();
        }
    };

    public static class Coordinate {
        private int x;

        private int y;

        public int x() { return x; }

        public int y() { return y; }

        private Coordinate() { }

        private Coordinate set(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        @Override
        public int hashCode() {
            int i = 31 + x;
            i = i * 31 + y;
            return i;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Coordinate) {
                Coordinate c = (Coordinate) obj;
                return x == c.x && y == c.y;
            }
            return false;
        }
    }

    private final Map<Coordinate, T> data = new HashMap<Coordinate, T>();

    public int size() { return data.size(); }

    public boolean isEmpty() { return data.isEmpty(); }

    public boolean hasAt(int x, int y) {
        Coordinate c = pool.obtain().set(x, y);
        boolean result = data.containsKey(c);
        pool.free(c);
        return result;
    }

    public boolean has(T value) { return data.containsValue(value); }

    public T get(int x, int y) {
        Coordinate c = pool.obtain().set(x, y);
        T result = data.get(c);
        pool.free(c);
        return result;
    }

    public T put(int x, int y, T value) {
        Coordinate c = pool.obtain().set(x, y);
        return data.put(c, value);
    }

    public T remove(int x, int y) {
        Coordinate c = pool.obtain().set(x, y);
        T result = data.remove(c);
        pool.free(c);
        return result;
    }

    public void clear() {
        for (Coordinate c : data.keySet()) {
            pool.free(c);
        }
        data.clear();
    }

    public Set<Coordinate> keys() {
        return data.keySet();
    }

    public Collection<T> values() {
        return data.values();
    }

}
