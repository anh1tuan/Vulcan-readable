package ac.vulcan.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class BukkitTaskHandle implements SchedulerBridge {
    private final BukkitTask bukkitTask;
    private final boolean repeating;

    public BukkitTaskHandle(BukkitTask bukkitTask) {
        this(bukkitTask, false);
    }

    public BukkitTaskHandle(BukkitTask bukkitTask, boolean repeating) {
        this.bukkitTask = bukkitTask;
        this.repeating = repeating;
    }

    @Override
    public void cancel() {
        this.bukkitTask.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.bukkitTask.isCancelled();
    }

    @Override
    public Plugin getOwner() {
        return this.bukkitTask.getOwner();
    }

    @Override
    public boolean isCurrentlyRunning() {
        return Bukkit.getServer().getScheduler().isCurrentlyRunning(this.bukkitTask.getTaskId());
    }

    @Override
    public boolean isRepeatingTask() {
        return this.repeating;
    }
}
