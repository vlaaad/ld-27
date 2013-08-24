package com.vlaaad.common.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created 24.08.13 by vlaaad
 */
public class EventDispatcher {
    private final ObjectMap<EventType, SnapshotArray> listenersMap = new ObjectMap<EventType, SnapshotArray>();

    public <T> void add(EventType<T> type, Listener<T> listener) {
        SnapshotArray<Listener<T>> list = getList(type, true);
        list.add(listener);
    }

    public <T> void remove(EventType<T> type, Listener<T> listener) {
        SnapshotArray<Listener<T>> list = getList(type, false);
        if (list == null)
            return;
        list.removeValue(listener, true);
    }

    public <T> void dispatch(EventType<T> type, T t) {
        SnapshotArray<Listener<T>> list = getList(type, false);
        if (list == null)
            return;
        list.begin();
        for (Listener<T> listener : list) {
            listener.handle(type, t);
        }
        list.end();
    }

    public void clear() {
        listenersMap.clear();
    }

    @SuppressWarnings("unchecked")
    private <T> SnapshotArray<Listener<T>> getList(EventType<T> type, boolean createIfNotExist) {
        SnapshotArray result = listenersMap.get(type);
        if (result == null) {
            if (!createIfNotExist)
                return null;
            SnapshotArray<Listener<T>> list = new SnapshotArray<Listener<T>>();
            listenersMap.put(type, list);
            return list;
        }
        return result;
    }
}
