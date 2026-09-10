package me.frep.vulcan.spigot;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class ObservableProperty {
    private final Set listeners = new HashSet();
    private Object value;

    public ObservableProperty(Object value) {
        this.value = value;
    }

    public Object get() {
        return this.value;
    }

    public void set(Object value) {
        Object previous = this.value;
        this.value = value;
        Iterator iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            ((PropertyChangeListener) iterator.next()).onChange(previous, value);
        }
    }

    public PropertyChangeListener addListener(PropertyChangeListener listener) {
        this.listeners.add(listener);
        return listener;
    }

    public void removeListener(PropertyChangeListener listener) {
        this.listeners.remove(listener);
    }
}
