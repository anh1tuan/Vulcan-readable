package me.frep.vulcan.spigot;

@FunctionalInterface
public interface PropertyChangeListener {
    void onChange(Object oldValue, Object newValue);
}
