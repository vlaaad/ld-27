package com.vlaaad.common.events;

/**
 * Created 24.08.13 by vlaaad
 */
public interface Listener<T> {
    public void handle(EventType<T> type, T t);
}
