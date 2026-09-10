package ac.vulcan.anticheat;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public interface SchedulerAdapter {
    boolean isGlobalTickThread();

    boolean isPrimaryThread();

    boolean isOwnedByCurrentRegion(Entity entity);

    boolean isOwnedByCurrentRegion(Location location);

    SchedulerBridge runTask(Runnable task);

    SchedulerBridge runTaskLater(Runnable task, long delayTicks);

    SchedulerBridge runTaskTimer(Runnable task, long delayTicks, long periodTicks);

    SchedulerBridge runTask(Plugin plugin, Runnable task);

    SchedulerBridge runTaskLater(Plugin plugin, Runnable task, long delayTicks);

    SchedulerBridge runTaskTimer(Plugin plugin, Runnable task, long delayTicks, long periodTicks);

    SchedulerBridge runTaskAtLocation(Location location, Runnable task);

    SchedulerBridge runTaskLaterAtLocation(Location location, Runnable task, long delayTicks);

    SchedulerBridge runTaskTimerAtLocation(Location location, Runnable task, long delayTicks, long periodTicks);

    SchedulerBridge runTaskForEntity(Entity entity, Runnable task);

    SchedulerBridge runTaskLaterForEntity(Entity entity, Runnable task, long delayTicks);

    SchedulerBridge runTaskTimerForEntity(Entity entity, Runnable task, long delayTicks, long periodTicks);

    SchedulerBridge runTaskAsynchronously(Runnable task);

    SchedulerBridge runTaskLaterAsynchronously(Runnable task, long delayTicks);

    SchedulerBridge runTaskTimerAsynchronously(Runnable task, long delayTicks, long periodTicks);

    void execute(Runnable task);

    void executeAtLocation(Location location, Runnable task);

    void executeForEntity(Entity entity, Runnable task);

    void cancelTasks();

    void cancelTasks(Plugin plugin);

    default boolean isGlobalTickThread(Object[] ignored) {
        return this.isGlobalTickThread();
    }

    default boolean isPrimaryThread(Object[] ignored) {
        return this.isPrimaryThread();
    }

    default boolean isOwnedByCurrentRegion(Object[] args) {
        if (args[0] instanceof Entity) {
            return this.isOwnedByCurrentRegion((Entity) args[0]);
        }
        return this.isOwnedByCurrentRegion((Location) args[0]);
    }

    default boolean isOwnedByRegionLocation(Object[] args) {
        return this.isOwnedByCurrentRegion((Location) args[0]);
    }

    default SchedulerBridge runTask(Object[] args) {
        if (args[0] instanceof Plugin) {
            return this.runTask((Plugin) args[0], (Runnable) args[1]);
        }
        return this.runTask((Runnable) args[0]);
    }

    default SchedulerBridge runTaskLater(Object[] args) {
        if (args[0] instanceof Runnable) {
            return this.runTaskLater((Runnable) args[0], ((Number) args[1]).longValue());
        }
        if (args[0] instanceof Plugin) {
            return this.runTaskLater((Plugin) args[0], (Runnable) args[1], ((Number) args[2]).longValue());
        }
        return this.runTaskLater((Runnable) args[1], ((Number) args[2]).longValue());
    }

    default SchedulerBridge runTaskTimer(Object[] args) {
        if (args[0] instanceof Runnable) {
            return this.runTaskTimer(
                (Runnable) args[0],
                ((Number) args[1]).longValue(),
                ((Number) args[2]).longValue()
            );
        }
        return this.runTaskTimer(
            (Plugin) args[0],
            (Runnable) args[2],
            ((Number) args[3]).longValue(),
            ((Number) args[4]).longValue()
        );
    }

    default SchedulerBridge runTaskForPlugin(Object[] args) {
        return this.runTask((Plugin) args[0], (Runnable) args[1]);
    }

    default SchedulerBridge runTaskLaterForPlugin(Object[] args) {
        return this.runTaskLater((Plugin) args[0], (Runnable) args[1], ((Number) args[2]).longValue());
    }

    default SchedulerBridge runTaskTimerForPlugin(Object[] args) {
        return this.runTaskTimer(
            (Plugin) args[0],
            (Runnable) args[2],
            ((Number) args[3]).longValue(),
            ((Number) args[4]).longValue()
        );
    }

    default SchedulerBridge runTaskAtLocation(Object[] args) {
        if (args[0] instanceof Location) {
            return this.runTaskAtLocation((Location) args[0], (Runnable) args[1]);
        }
        return this.runTaskLater((Plugin) args[0], (Runnable) args[1], ((Number) args[2]).longValue());
    }

    default SchedulerBridge runTaskLaterAtLocation(Object[] args) {
        return this.runTaskLaterAtLocation((Location) args[0], (Runnable) args[2], ((Number) args[3]).longValue());
    }

    default SchedulerBridge runTaskTimerAtLocation(Object[] args) {
        return this.runTaskTimerAtLocation(
            (Location) args[0],
            (Runnable) args[2],
            ((Number) args[3]).longValue(),
            ((Number) args[4]).longValue()
        );
    }

    default SchedulerBridge runTaskForEntity(Object[] args) {
        return this.runTaskForEntity((Entity) args[0], (Runnable) args[1]);
    }

    default SchedulerBridge runTaskLaterForEntity(Object[] args) {
        return this.runTaskLaterForEntity((Entity) args[0], (Runnable) args[2], ((Number) args[3]).longValue());
    }

    default SchedulerBridge runTaskTimerForEntity(Object[] args) {
        return this.runTaskTimerForEntity(
            (Entity) args[1],
            (Runnable) args[2],
            ((Number) args[3]).longValue(),
            ((Number) args[4]).longValue()
        );
    }

    default SchedulerBridge runTaskAsynchronously(Object[] args) {
        if (args[0] instanceof Plugin) {
            return this.runTaskAsynchronously((Runnable) args[1]);
        }
        return this.runTaskAsynchronously((Runnable) args[0]);
    }

    default SchedulerBridge runTaskLaterAsynchronously(Object[] args) {
        if (args[0] instanceof Runnable) {
            return this.runTaskLaterAsynchronously((Runnable) args[0], ((Number) args[1]).longValue());
        }
        return this.runTaskLaterAsynchronously((Runnable) args[1], ((Number) args[2]).longValue());
    }

    default SchedulerBridge runTaskTimerAsynchronously(Object[] args) {
        return this.runTaskTimerAsynchronously(
            (Runnable) args[0],
            ((Number) args[1]).longValue(),
            ((Number) args[2]).longValue()
        );
    }

    default SchedulerBridge runTaskAsynchronouslyForPlugin(Object[] args) {
        return this.runTaskAsynchronously((Runnable) args[1]);
    }

    default SchedulerBridge runTaskLaterAsynchronouslyForPlugin(Object[] args) {
        return this.runTaskLaterAsynchronously((Runnable) args[1], ((Number) args[2]).longValue());
    }

    default SchedulerBridge runTaskTimerAsynchronouslyForPlugin(Object[] args) {
        return this.runTaskTimerAsynchronously(
            (Runnable) args[1],
            ((Number) args[2]).longValue(),
            ((Number) args[3]).longValue()
        );
    }

    default Future callSyncMethod(Callable callable) {
        CompletableFuture future = new CompletableFuture();
        this.execute(() -> {
            try {
                future.complete(callable.call());
            } catch (Exception exception) {
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    default Future callSyncMethod(Object[] args) {
        return this.callSyncMethod((Callable) args[0]);
    }

    default void executeGlobal(Object[] args) {
        if (args[0] instanceof Location) {
            this.executeAtLocation((Location) args[0], (Runnable) args[1]);
            return;
        }
        this.execute((Runnable) args[0]);
    }

    default void executeAtLocation(Object[] args) {
        if (args[0] instanceof Entity) {
            this.executeForEntity((Entity) args[0], (Runnable) args[1]);
            return;
        }
        this.executeAtLocation((Location) args[0], (Runnable) args[1]);
    }

    default void executeForEntity(Object[] args) {
        if (args.length == 0) {
            this.cancelTasks();
            return;
        }
        if (args[0] instanceof Entity) {
            this.executeForEntity((Entity) args[0], (Runnable) args[1]);
            return;
        }
        this.cancelTasks();
    }

    default void cancelPluginTasks(Object[] args) {
        if (args.length == 0) {
            this.cancelTasks();
            return;
        }
        this.cancelTasks((Plugin) args[0]);
    }

    default void cancelTasksForPlugin(Object[] args) {
        this.cancelTasks((Plugin) args[0]);
    }
}
