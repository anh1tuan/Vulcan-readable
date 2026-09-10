package ac.vulcan.anticheat;

import org.bukkit.plugin.Plugin;


public interface SchedulerBridge {
    void cancel();

    boolean isCancelled();

    Plugin getOwner();

    boolean isCurrentlyRunning();

    boolean isRepeatingTask();

    default void cancel(Object[] ignored) {
        this.cancel();
    }

    default boolean isCancelled(Object[] ignored) {
        return this.isCancelled();
    }

    default Plugin getOwner(Object[] ignored) {
        return this.getOwner();
    }

    default boolean isCurrentlyRunning(Object[] ignored) {
        return this.isCurrentlyRunning();
    }

    default boolean isRepeatingTask(Object[] ignored) {
        return this.isRepeatingTask();
    }

    default void setSizeStartText() {
        this.cancel();
    }
}
