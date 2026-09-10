package ac.vulcan.anticheat;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class FoliaSchedulerAdapter implements SchedulerAdapter {
    private final Plugin plugin;
    private final RegionScheduler regionScheduler;
    private final GlobalRegionScheduler globalScheduler;
    private final AsyncScheduler asyncScheduler;

    public FoliaSchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
        this.regionScheduler = Bukkit.getServer().getRegionScheduler();
        this.globalScheduler = Bukkit.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = Bukkit.getServer().getAsyncScheduler();
    }

    private static long atLeastOne(long ticks) {
        return ticks <= 0L ? 1L : ticks;
    }

    private static void run(Runnable task) {
        task.run();
    }

    @Override
    public boolean isGlobalTickThread() {
        return Bukkit.getServer().isGlobalTickThread();
    }

    @Override
    public boolean isPrimaryThread() {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(Entity entity) {
        return Bukkit.getServer().isOwnedByCurrentRegion(entity);
    }

    @Override
    public boolean isOwnedByCurrentRegion(Location location) {
        return Bukkit.getServer().isOwnedByCurrentRegion(location);
    }

    @Override
    public SchedulerBridge runTask(Runnable task) {
        return new FoliaTaskHandle(this.globalScheduler.run(this.plugin, scheduled -> run(task)));
    }

    @Override
    public SchedulerBridge runTaskLater(Runnable task, long delayTicks) {
        if (delayTicks <= 0L) {
            return this.runTask(task);
        }
        return new FoliaTaskHandle(this.globalScheduler.runDelayed(this.plugin, scheduled -> run(task), delayTicks));
    }

    @Override
    public SchedulerBridge runTaskTimer(Runnable task, long delayTicks, long periodTicks) {
        return new FoliaTaskHandle(this.globalScheduler.runAtFixedRate(
            this.plugin,
            scheduled -> run(task),
            atLeastOne(delayTicks),
            atLeastOne(periodTicks)
        ));
    }

    @Override
    public SchedulerBridge runTask(Plugin plugin, Runnable task) {
        return new FoliaTaskHandle(this.globalScheduler.run(plugin, scheduled -> run(task)));
    }

    @Override
    public SchedulerBridge runTaskLater(Plugin plugin, Runnable task, long delayTicks) {
        if (delayTicks <= 0L) {
            return this.runTask(plugin, task);
        }
        return new FoliaTaskHandle(this.globalScheduler.runDelayed(plugin, scheduled -> run(task), delayTicks));
    }

    @Override
    public SchedulerBridge runTaskTimer(Plugin plugin, Runnable task, long delayTicks, long periodTicks) {
        return new FoliaTaskHandle(this.globalScheduler.runAtFixedRate(
            plugin,
            scheduled -> run(task),
            atLeastOne(delayTicks),
            atLeastOne(periodTicks)
        ));
    }

    @Override
    public SchedulerBridge runTaskAtLocation(Location location, Runnable task) {
        return new FoliaTaskHandle(this.regionScheduler.run(this.plugin, location, scheduled -> run(task)));
    }

    @Override
    public SchedulerBridge runTaskLaterAtLocation(Location location, Runnable task, long delayTicks) {
        if (delayTicks <= 0L) {
            return this.runTaskAtLocation(location, task);
        }
        return new FoliaTaskHandle(this.regionScheduler.runDelayed(this.plugin, location, scheduled -> run(task), delayTicks));
    }

    @Override
    public SchedulerBridge runTaskTimerAtLocation(Location location, Runnable task, long delayTicks, long periodTicks) {
        return new FoliaTaskHandle(this.regionScheduler.runAtFixedRate(
            this.plugin,
            location,
            scheduled -> run(task),
            atLeastOne(delayTicks),
            atLeastOne(periodTicks)
        ));
    }

    @Override
    public SchedulerBridge runTaskForEntity(Entity entity, Runnable task) {
        return new FoliaTaskHandle(entity.getScheduler().run(this.plugin, scheduled -> run(task), null));
    }

    @Override
    public SchedulerBridge runTaskLaterForEntity(Entity entity, Runnable task, long delayTicks) {
        if (delayTicks <= 0L) {
            return this.runTaskForEntity(entity, task);
        }
        return new FoliaTaskHandle(entity.getScheduler().runDelayed(this.plugin, scheduled -> run(task), null, delayTicks));
    }

    @Override
    public SchedulerBridge runTaskTimerForEntity(Entity entity, Runnable task, long delayTicks, long periodTicks) {
        return new FoliaTaskHandle(entity.getScheduler().runAtFixedRate(
            this.plugin,
            scheduled -> run(task),
            null,
            atLeastOne(delayTicks),
            atLeastOne(periodTicks)
        ));
    }

    @Override
    public SchedulerBridge runTaskAsynchronously(Runnable task) {
        return new FoliaTaskHandle(this.asyncScheduler.runNow(this.plugin, scheduled -> run(task)));
    }

    @Override
    public SchedulerBridge runTaskLaterAsynchronously(Runnable task, long delayTicks) {
        long delayMs = atLeastOne(delayTicks) * 50L;
        return new FoliaTaskHandle(this.asyncScheduler.runDelayed(this.plugin, scheduled -> run(task), delayMs, TimeUnit.MILLISECONDS));
    }

    @Override
    public SchedulerBridge runTaskTimerAsynchronously(Runnable task, long delayTicks, long periodTicks) {
        return new FoliaTaskHandle(this.asyncScheduler.runAtFixedRate(
            this.plugin,
            scheduled -> run(task),
            atLeastOne(delayTicks) * 50L,
            atLeastOne(periodTicks) * 50L,
            TimeUnit.MILLISECONDS
        ));
    }

    @Override
    public void execute(Runnable task) {
        this.globalScheduler.execute(this.plugin, task);
    }

    @Override
    public void executeAtLocation(Location location, Runnable task) {
        this.regionScheduler.execute(this.plugin, location, task);
    }

    @Override
    public void executeForEntity(Entity entity, Runnable task) {
        entity.getScheduler().execute(this.plugin, task, null, 1L);
    }

    @Override
    public void cancelTasks() {
        this.globalScheduler.cancelTasks(this.plugin);
        this.asyncScheduler.cancelTasks(this.plugin);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        this.globalScheduler.cancelTasks(plugin);
        this.asyncScheduler.cancelTasks(plugin);
    }
}
