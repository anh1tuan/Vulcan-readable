package ac.vulcan.anticheat;

import org.bukkit.plugin.Plugin;

public abstract class UniversalRunnable implements Runnable {
    private SchedulerBridge scheduledTask;

    public synchronized void cancel() {
        this.checkScheduled();
        this.scheduledTask.cancel();
    }

    public synchronized void cancel(Object[] ignored) {
        this.cancel();
    }

    public synchronized boolean isCancelled() {
        this.checkScheduled();
        return this.scheduledTask.isCancelled();
    }

    public synchronized boolean isCancelled(Object[] ignored) {
        return this.isCancelled();
    }

    public synchronized SchedulerBridge runTask(Plugin plugin) {
        this.checkNotYetScheduled();
        return this.setupTask(UniversalScheduler.createSchedulerAdapter(plugin).runTask(this));
    }

    public synchronized SchedulerBridge runTask(Object[] args) {
        return this.runTask((Plugin) args[0]);
    }

    public synchronized SchedulerBridge runTaskAsynchronously(Plugin plugin) {
        this.checkNotYetScheduled();
        return this.setupTask(UniversalScheduler.createSchedulerAdapter(plugin).runTaskAsynchronously(this));
    }

    public synchronized SchedulerBridge runTaskAsynchronously(Object[] args) {
        return this.runTaskAsynchronously((Plugin) args[0]);
    }

    public synchronized SchedulerBridge runTaskLater(Plugin plugin, long delayTicks) {
        this.checkNotYetScheduled();
        return this.setupTask(UniversalScheduler.createSchedulerAdapter(plugin).runTaskLater(this, delayTicks));
    }

    public synchronized SchedulerBridge runTaskLater(Object[] args) {
        return this.runTaskLater((Plugin) args[0], ((Number) args[1]).longValue());
    }

    public synchronized SchedulerBridge runTaskLaterAsynchronously(Plugin plugin, long delayTicks) {
        this.checkNotYetScheduled();
        return this.setupTask(UniversalScheduler.createSchedulerAdapter(plugin).runTaskLaterAsynchronously(this, delayTicks));
    }

    public synchronized SchedulerBridge runTaskLaterAsynchronously(Object[] args) {
        if (args[0] instanceof Plugin) {
            return this.runTaskLaterAsynchronously((Plugin) args[0], ((Number) args[1]).longValue());
        }
        return this.runTaskLaterAsynchronously((Plugin) args[1], ((Number) args[2]).longValue());
    }

    public synchronized SchedulerBridge runTaskTimer(Plugin plugin, long delayTicks, long periodTicks) {
        this.checkNotYetScheduled();
        return this.setupTask(UniversalScheduler.createSchedulerAdapter(plugin).runTaskTimer(this, delayTicks, periodTicks));
    }

    public synchronized SchedulerBridge runTaskTimer(Object[] args) {
        return this.runTaskTimer(
            (Plugin) args[0],
            ((Number) args[1]).longValue(),
            ((Number) args[2]).longValue()
        );
    }

    public synchronized SchedulerBridge runTaskTimerAsynchronously(Plugin plugin, long delayTicks, long periodTicks) {
        this.checkNotYetScheduled();
        return this.setupTask(
            UniversalScheduler.createSchedulerAdapter(plugin).runTaskTimerAsynchronously(this, delayTicks, periodTicks)
        );
    }

    public synchronized SchedulerBridge runTaskTimerAsynchronously(Object[] args) {
        return this.runTaskTimerAsynchronously(
            (Plugin) args[0],
            ((Number) args[1]).longValue(),
            ((Number) args[2]).longValue()
        );
    }

    private void checkScheduled() {
        if (this.scheduledTask == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
    }

    private void checkNotYetScheduled() {
        if (this.scheduledTask != null) {
            throw new IllegalStateException("Already scheduled");
        }
    }

    private SchedulerBridge setupTask(SchedulerBridge task) {
        this.scheduledTask = task;
        return task;
    }

    private SchedulerBridge setupTask(Object[] args) {
        return this.setupTask((SchedulerBridge) args[0]);
    }
}
