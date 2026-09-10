package ac.vulcan.anticheat;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public final class FoliaTaskHandle implements SchedulerBridge {
    private final ScheduledTask scheduledTask;

    public FoliaTaskHandle(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @Override
    public void cancel() {
        this.scheduledTask.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.scheduledTask.isCancelled();
    }

    @Override
    public Plugin getOwner() {
        return this.scheduledTask.getOwningPlugin();
    }

    @Override
    public boolean isCurrentlyRunning() {
        return this.scheduledTask.getExecutionState() == ScheduledTask.ExecutionState.RUNNING;
    }

    @Override
    public boolean isRepeatingTask() {
        return this.scheduledTask.isRepeatingTask();
    }
}
