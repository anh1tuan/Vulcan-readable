package ac.vulcan.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class BukkitSchedulerAdapter implements SchedulerAdapter {
    private final Plugin plugin;

    public BukkitSchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isGlobalTickThread() {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public boolean isPrimaryThread() {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(Entity entity) {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(Location location) {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public SchedulerBridge runTask(Runnable task) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTask(this.plugin, task), false);
    }

    @Override
    public SchedulerBridge runTaskLater(Runnable task, long delayTicks) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLater(this.plugin, task, delayTicks), false);
    }

    @Override
    public SchedulerBridge runTaskTimer(Runnable task, long delayTicks, long periodTicks) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimer(this.plugin, task, delayTicks, periodTicks), true);
    }

    @Override
    public SchedulerBridge runTask(Plugin plugin, Runnable task) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTask(plugin, task), false);
    }

    @Override
    public SchedulerBridge runTaskLater(Plugin plugin, Runnable task, long delayTicks) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks), false);
    }

    @Override
    public SchedulerBridge runTaskTimer(Plugin plugin, Runnable task, long delayTicks, long periodTicks) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks), true);
    }

    @Override
    public SchedulerBridge runTaskAtLocation(Location location, Runnable task) {
        return this.runTask(task);
    }

    @Override
    public SchedulerBridge runTaskLaterAtLocation(Location location, Runnable task, long delayTicks) {
        return this.runTaskLater(task, delayTicks);
    }

    @Override
    public SchedulerBridge runTaskTimerAtLocation(Location location, Runnable task, long delayTicks, long periodTicks) {
        return this.runTaskTimer(task, delayTicks, periodTicks);
    }

    @Override
    public SchedulerBridge runTaskForEntity(Entity entity, Runnable task) {
        return this.runTask(task);
    }

    @Override
    public SchedulerBridge runTaskLaterForEntity(Entity entity, Runnable task, long delayTicks) {
        return this.runTaskLater(task, delayTicks);
    }

    @Override
    public SchedulerBridge runTaskTimerForEntity(Entity entity, Runnable task, long delayTicks, long periodTicks) {
        return this.runTaskTimer(task, delayTicks, periodTicks);
    }

    @Override
    public SchedulerBridge runTaskAsynchronously(Runnable task) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskAsynchronously(this.plugin, task), false);
    }

    @Override
    public SchedulerBridge runTaskLaterAsynchronously(Runnable task, long delayTicks) {
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, task, delayTicks), false);
    }

    @Override
    public SchedulerBridge runTaskTimerAsynchronously(Runnable task, long delayTicks, long periodTicks) {
        return new BukkitTaskHandle(
            Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, task, delayTicks, periodTicks),
            true
        );
    }

    @Override
    public void execute(Runnable task) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, task);
    }

    @Override
    public void executeAtLocation(Location location, Runnable task) {
        this.execute(task);
    }

    @Override
    public void executeForEntity(Entity entity, Runnable task) {
        this.execute(task);
    }

    @Override
    public void cancelTasks() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
